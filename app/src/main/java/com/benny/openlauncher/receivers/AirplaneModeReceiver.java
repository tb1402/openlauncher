package com.benny.openlauncher.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.benny.openlauncher.util.AdbConnectionManager;
import com.benny.openlauncher.util.AppSettings;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AirplaneModeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!intent.getAction().equals(Intent.ACTION_AIRPLANE_MODE_CHANGED) || !AppSettings.get().isAirplaneModeToggleLocked()) return;
        if (intent.getBooleanExtra("state", false)) return;//true=on

        ExecutorService es = Executors.newSingleThreadExecutor();
        es.submit(() -> {
            try {
                Thread.sleep(1000);

                AdbConnectionManager cm = AdbConnectionManager.getInstance(context);
                cm.connect(5555);
                cm.openStream("shell:cmd connectivity airplane-mode enable").close();
            } catch (Exception ignored) {
            }
        });
        es.shutdown();
    }
}
