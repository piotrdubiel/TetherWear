package io.wear.tether.service;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import io.wear.tether.messages.ConfigurationModel;

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
                setWifiEnabled(false);
                ok(node, setWifiTetheringEnabled(true));
            } else if (messageEvent.getPath().equals(REQUEST_WIFI_TETHER_OFF)) {
                setWifiTetheringEnabled(false);
                setWifiEnabled(true);
                ok(node, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Tether", e.toString());
            fail(node);
        }
    }

    private ConfigurationModel setWifiTetheringEnabled(boolean enable) throws InvocationTargetException, IllegalAccessException {
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);

        Method[] methods = wifiManager.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals("setWifiApEnabled")) {
                method.invoke(wifiManager, null, enable);
                break;
            }
        }
        if (enable) {
            for (Method m : methods) {
                if (m.getName().equals("getWifiApConfiguration")) {
                    WifiConfiguration config = (WifiConfiguration) m.invoke(wifiManager);
                    return new ConfigurationModel(config.SSID, config.preSharedKey);
                }
            }
        }
        return null;
    }

    private void setWifiEnabled(boolean enable) {
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        wifiManager.setWifiEnabled(enable);
    }

    private void ok(String node, ConfigurationModel config) {
        byte[] data;
        if (config != null) {
            data = config.getBytes();
        }
        else {
            data = new byte[0];
        }

        Wearable.MessageApi.sendMessage(googleApiClient, node, RESULT_SUCCESS, data);
    }

    private void fail(String node) {
        Wearable.MessageApi.sendMessage(googleApiClient, node, RESULT_FAILURE, new byte[0]);
    }
}
