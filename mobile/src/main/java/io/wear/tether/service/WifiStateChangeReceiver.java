package io.wear.tether.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import io.wear.tether.messages.ConfigurationModel;
import io.wear.tether.messages.MessageConstants;

public class WifiStateChangeReceiver extends BroadcastReceiver {
    private final static String TAG = "WifiStateChangeReceiver";
    public static final int WIFI_AP_STATE_DISABLING = 10;
    public static final int WIFI_AP_STATE_DISABLED = 11;
    public static final int WIFI_AP_STATE_ENABLING = 12;
    public static final int WIFI_AP_STATE_ENABLED = 13;
    public static final int WIFI_AP_STATE_FAILED = 14;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                        .addApi(Wearable.API)
                        .build();
                googleApiClient.blockingConnect();
                String action = intent.getAction();
                Log.d(TAG, action);

                List<Node> nodes = Wearable.NodeApi.getConnectedNodes(googleApiClient).await().getNodes();

                if ("android.net.wifi.WIFI_AP_STATE_CHANGED".equals(action)) {
                    int state = intent.getIntExtra("wifi_state", 0);

                    switch (state) {
                        case WIFI_AP_STATE_ENABLED:
                            Log.d("STATE", "Enabled");
                            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                            Method[] methods = wifiManager.getClass().getDeclaredMethods();
                            for (Method m : methods) {
                                if (m.getName().equals("getWifiApConfiguration")) {
                                    WifiConfiguration config = null;
                                    try {
                                        config = (WifiConfiguration) m.invoke(wifiManager);
                                        sendToNodes(googleApiClient, nodes, MessageConstants.STATE_WIFI_TETHER_ON, new ConfigurationModel(config.SSID, config.preSharedKey).getBytes());
                                    } catch (IllegalAccessException | InvocationTargetException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            break;
                        case WIFI_AP_STATE_DISABLED:
                            Log.d("STATE", "Disabled");
                            sendToNodes(googleApiClient, nodes, MessageConstants.STATE_WIFI_TETHER_OFF, null);
                            break;
                        case WIFI_AP_STATE_FAILED:
                            Log.d("STATE", "Failed");
                            break;
                    }

                    Log.d(TAG, String.valueOf(state));
                }
            }
        }).start();
    }

    private static void sendToNodes(GoogleApiClient googleApiClient, List<Node> nodes, String message, byte[] data) {
        for (Node node : nodes) {
            Wearable.MessageApi.sendMessage(googleApiClient, node.getId(), message, data);
        }
    }
}
