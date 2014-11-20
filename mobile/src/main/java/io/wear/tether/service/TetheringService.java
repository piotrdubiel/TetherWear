package io.wear.tether.service;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import io.wear.tether.R;

import static io.wear.tether.messages.MessageConstants.*;


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
        if (messageEvent.getPath().equals(REQUEST_WIFI_TETHER_ON)) {
            String node = messageEvent.getSourceNodeId();
            try {
                setWifiTetheringEnabled(true);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                Log.d("Tether", e.toString());
                fail(node);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                Log.d("Tether", e.toString());
                fail(node);
            }
            ok(node);
        }
    }

    private void setWifiTetheringEnabled(boolean enable) throws InvocationTargetException, IllegalAccessException {
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        wifiManager.setWifiEnabled(false);

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
