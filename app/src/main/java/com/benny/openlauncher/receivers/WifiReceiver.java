package com.benny.openlauncher.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

import com.benny.openlauncher.util.AdbConnectionManager;
import com.benny.openlauncher.util.AppSettings;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WifiReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION) || !AppSettings.get().isWifiToggleLocked()) return;

        int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
        if (state == WifiManager.WIFI_STATE_ENABLING || state == WifiManager.WIFI_STATE_ENABLED) return;

        ExecutorService es = Executors.newSingleThreadExecutor();
        es.submit(() -> {
            try {
                Thread.sleep(1000);

                AdbConnectionManager cm = AdbConnectionManager.getInstance(context);
                cm.connect(5555);
                cm.openStream("shell:svc wifi enable").close();
            } catch (Exception ignored) {
            }
        });
        es.shutdown();
    }
}
