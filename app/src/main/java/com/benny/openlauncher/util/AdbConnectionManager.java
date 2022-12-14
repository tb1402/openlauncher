package com.benny.openlauncher.util;

import android.content.Context;
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
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
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

    public synchronized static AdbConnectionManager getInstance(Context c) throws Exception {
        if (INSTANCE == null) {
            c = c.getApplicationContext();
            File f = new File(c.getFilesDir() + "/cert");
            if (!f.exists()) return INSTANCE = new AdbConnectionManager(c, null, null);

            ObjectInputStream oi = new ObjectInputStream(new FileInputStream(f));
            Certificate cert = (Certificate) oi.readObject();
            oi.close();

            f = new File(c.getFilesDir() + "/pk");
            if (!f.exists()) return INSTANCE = new AdbConnectionManager(c, null, null);

            oi = new ObjectInputStream(new FileInputStream(f));
            PrivateKey key = (PrivateKey) oi.readObject();
            oi.close();
            return INSTANCE = new AdbConnectionManager(c, key, cert);
        }
        return INSTANCE;
    }

    private PrivateKey mPrivateKey;
    private Certificate mCertificate;

    private AdbConnectionManager(Context c, PrivateKey privateKey, Certificate cert) throws Exception {
        // Set the API version whose `adbd` is running
        setApi(Build.VERSION.SDK_INT);

        mPrivateKey = privateKey;
        mCertificate = cert;
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
            String algorithmName = "SHA512withRSA";
            long expiryDate = System.currentTimeMillis() + 86400000;
            CertificateExtensions certificateExtensions = new CertificateExtensions();
            certificateExtensions.set("SubjectKeyIdentifier", new SubjectKeyIdentifierExtension(
                    new KeyIdentifier(publicKey).getIdentifier()));
            X500Name x500Name = new X500Name("CN=Launcher local");
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

            asyncTaskWriteObjectToFile.writeFileStatus wfs = (success1, name) -> Log.d("TBL", success1 + name);
            new asyncTaskWriteObjectToFile(c, "pk", mPrivateKey, wfs).execute();
            new asyncTaskWriteObjectToFile(c, "cert", mCertificate, wfs).execute();
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
        return "Launcher local";
    }
}
