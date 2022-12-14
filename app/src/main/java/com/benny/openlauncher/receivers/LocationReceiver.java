package com.benny.openlauncher.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;

import com.benny.openlauncher.util.AdbConnectionManager;
import com.benny.openlauncher.util.AppSettings;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LocationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!intent.getAction().equals(LocationManager.MODE_CHANGED_ACTION) || !AppSettings.get().isLocationToggleLocked()) return;

        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (!lm.isLocationEnabled()) return;
        } else {
            if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) return;
        }

        try {
            Settings.Secure.putInt(context.getContentResolver(), "location_mode", 0);
        } catch (Exception e) {
            e.printStackTrace();
            ExecutorService es = Executors.newSingleThreadExecutor();
            es.submit(() -> {
                try {
                    Thread.sleep(1000);

                    AdbConnectionManager cm = AdbConnectionManager.getInstance(context);
                    cm.connect(5555);
                    cm.openStream("shell:settings put secure location_mode 0").close();
                } catch (Exception ignored) {
                }
            });
            es.shutdown();
        }
    }
}
