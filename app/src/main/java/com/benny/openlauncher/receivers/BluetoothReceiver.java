package com.benny.openlauncher.receivers;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.benny.openlauncher.util.AppSettings;

public class BluetoothReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!intent.getAction().equals(BluetoothAdapter.ACTION_STATE_CHANGED) || !AppSettings.get().isBluetoothToggleLocked()) return;

        int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
        if (state == BluetoothAdapter.STATE_TURNING_OFF || state == BluetoothAdapter.STATE_OFF) return;

        try {
            BluetoothAdapter.getDefaultAdapter().disable();
        }catch (SecurityException ignored){
        }
    }
}
