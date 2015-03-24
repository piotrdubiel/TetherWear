package io.wear.tether.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class WifiStateChangeReceiver extends BroadcastReceiver {
    private final static String TAG = "WifiStateChangeReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, action);
        if ("android.net.wifi.WIFI_AP_STATE_CHANGED".equals(action)) {

            // get Wi-Fi Hotspot state here
            int state = intent.getIntExtra("wifi_state", 0);

            Log.d(TAG, String.valueOf(state));
        }
    }
}
