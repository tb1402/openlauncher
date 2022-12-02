package com.benny.openlauncher.util;

import android.os.Build;
import android.sun.security.x509.AlgorithmId;
import android.sun.security.x509.CertificateAlgorithmId;
import android.sun.security.x509.CertificateExtensions;
import android.sun.security.x509.CertificateIssuerName;
import android.sun.security.x509.CertificateSerialNumber;
import android.sun.security.x509.CertificateSubjectName;
import android.sun.security.x509.CertificateValidity;
import android.sun.security.x509.CertificateVersion;
import android.sun.security.x509.CertificateX509Key;
import android.sun.security.x509.KeyIdentifier;
import android.sun.security.x509.PrivateKeyUsageExtension;
import android.sun.security.x509.SubjectKeyIdentifierExtension;
import android.sun.security.x509.X500Name;
import android.sun.security.x509.X509CertImpl;
import android.sun.security.x509.X509CertInfo;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.util.Date;
import java.util.Random;

import io.github.muntashirakon.adb.AbsAdbConnectionManager;

public class AdbConnectionManager extends AbsAdbConnectionManager {
    private static AdbConnectionManager INSTANCE;

    public static AdbConnectionManager getInstance() throws Exception {
        if (INSTANCE == null) {
            INSTANCE = new AdbConnectionManager();
        }
        return INSTANCE;
    }

    private PrivateKey mPrivateKey;
    private Certificate mCertificate;

    private AdbConnectionManager() throws Exception {
        // Set the API version whose `adbd` is running
        setApi(Build.VERSION.SDK_INT);
        // TODO: Load private key and certificate (along with public key) from
        //  some place such as KeyStore or file system.
        mPrivateKey = null;
        mCertificate = null;
        if (mPrivateKey == null) {
            // Generate a new key pair
            int keySize = 2048;
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(keySize, SecureRandom.getInstance("SHA1PRNG"));
            KeyPair generateKeyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = generateKeyPair.getPublic();
            mPrivateKey = generateKeyPair.getPrivate();
            // Generate a certificate
            // On Android, it requires sun.security-android library as mentioned
            // above.
            String subject = "CN=My Awesome App";
            String algorithmName = "SHA512withRSA";
            long expiryDate = System.currentTimeMillis() + 86400000;
            CertificateExtensions certificateExtensions = new CertificateExtensions();
            certificateExtensions.set("SubjectKeyIdentifier", new SubjectKeyIdentifierExtension(
                    new KeyIdentifier(publicKey).getIdentifier()));
            X500Name x500Name = new X500Name(subject);
            Date notBefore = new Date();
            Date notAfter = new Date(expiryDate);
            certificateExtensions.set("PrivateKeyUsage", new PrivateKeyUsageExtension(notBefore, notAfter));
            CertificateValidity certificateValidity = new CertificateValidity(notBefore, notAfter);
            X509CertInfo x509CertInfo = new X509CertInfo();
            x509CertInfo.set("version", new CertificateVersion(2));
            x509CertInfo.set("serialNumber", new CertificateSerialNumber(new Random().nextInt() & Integer.MAX_VALUE));
            x509CertInfo.set("algorithmID", new CertificateAlgorithmId(AlgorithmId.get(algorithmName)));
            x509CertInfo.set("subject", new CertificateSubjectName(x500Name));
            x509CertInfo.set("key", new CertificateX509Key(publicKey));
            x509CertInfo.set("validity", certificateValidity);
            x509CertInfo.set("issuer", new CertificateIssuerName(x500Name));
            x509CertInfo.set("extensions", certificateExtensions);
            X509CertImpl x509CertImpl = new X509CertImpl(x509CertInfo);
            x509CertImpl.sign(mPrivateKey, algorithmName);
            mCertificate = x509CertImpl;
            // TODO: Store the key pair to some place else.
        }
    }

    @Override
    protected PrivateKey getPrivateKey() {
        return mPrivateKey;
    }

    @Override
    protected Certificate getCertificate() {
        return mCertificate;
    }

    @Override
    protected String getDeviceName() {
        return "MyAwesomeApp";
    }
}
