package com.benny.openlauncher.util;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import androidx.annotation.Nullable;

public class AccessibilityService extends android.accessibilityservice.AccessibilityService {
    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        Log.d("TBL", accessibilityEvent.getAction() + "");
        if (accessibilityEvent.getEventType() != AccessibilityEvent.TYPE_TOUCH_INTERACTION_END) return;

        if (accessibilityEvent.getAction() == 4) performGlobalAction(
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ? GLOBAL_ACTION_DISMISS_NOTIFICATION_SHADE : GLOBAL_ACTION_HOME);
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
    }
}
