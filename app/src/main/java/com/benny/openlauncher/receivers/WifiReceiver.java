package com.benny.openlauncher.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.benny.openlauncher.util.AdbConnectionManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WifiReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wm.isWifiEnabled()) return;
        Log.d("OL", "disabled");
        wm.setWifiEnabled(true);

        ExecutorService es= Executors.newSingleThreadExecutor();
        es.submit(() -> {
            try {
                AdbConnectionManager.getInstance().connect("localhost", 5555);
                AdbConnectionManager.getInstance().openStream("shell:svc wifi enable").close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        es.shutdown();
    }
}
