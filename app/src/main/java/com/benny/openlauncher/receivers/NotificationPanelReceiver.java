package com.benny.openlauncher.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

import com.benny.openlauncher.util.AppSettings;

public class NotificationPanelReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!AppSettings.get().isNotificationPanelLocked()) return;
        Log.d("TBL", intent.getAction());

        AccessibilityEvent evt = AccessibilityEvent.obtain(AccessibilityEvent.TYPE_TOUCH_INTERACTION_END);
        evt.getText().add("noti");
        evt.setAction(4);
        evt.setEnabled(true);
        AccessibilityManager manager = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        manager.sendAccessibilityEvent(evt);
    }
}
