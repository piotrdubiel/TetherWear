package io.wear.tether.service;

import android.net.wifi.WifiManager;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static io.wear.tether.messages.MessageConstants.REQUEST_WIFI_TETHER_OFF;
import static io.wear.tether.messages.MessageConstants.REQUEST_WIFI_TETHER_ON;
import static io.wear.tether.messages.MessageConstants.RESULT_FAILURE;
import static io.wear.tether.messages.MessageConstants.RESULT_SUCCESS;


public class TetheringService extends WearableListenerService {
    GoogleApiClient googleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        String node = messageEvent.getSourceNodeId();
        try {
            if (messageEvent.getPath().equals(REQUEST_WIFI_TETHER_ON)) {
                setWifiTetheringEnabled(true);
            }
            else if (messageEvent.getPath().equals(REQUEST_WIFI_TETHER_OFF)) {
                setWifiTetheringEnabled(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Tether", e.toString());
            fail(node);
        }
        ok(node);
    }

    private void setWifiTetheringEnabled(boolean enable) throws InvocationTargetException, IllegalAccessException {
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        wifiManager.setWifiEnabled(!enable);

        Method[] methods = wifiManager.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals("setWifiApEnabled")) {
                method.invoke(wifiManager, null, enable);
                break;
            }
        }
    }

    private void ok(String node) {
        Wearable.MessageApi.sendMessage(googleApiClient, node, RESULT_SUCCESS, new byte[0]);
    }

    private void fail(String node) {
        Wearable.MessageApi.sendMessage(googleApiClient, node, RESULT_FAILURE, new byte[0]);
    }
}
