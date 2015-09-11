package io.wear.tether

import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import io.state.machine.StateMachine
import io.wear.tether.messages.ConfigurationModel
import io.wear.tether.messages.MessageConstants
import io.wear.tether.state.ChangeHotspotState
import io.wear.tether.state.DisconnectedState

public class ListenerService : WearableListenerService() {
    override fun onMessageReceived(messageEvent: MessageEvent?) {
        super.onMessageReceived(messageEvent)
        if (messageEvent == null) {
            return
        }
        try {
            if (messageEvent.getPath() == MessageConstants.STATE_WIFI_TETHER_ON) {
                createNotification(this, ConfigurationModel.fromBytes(messageEvent.getData()))
            } else if (messageEvent.getPath() == MessageConstants.STATE_WIFI_TETHER_OFF) {
                NotificationManagerCompat.from(this).cancelAll()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("Tether", e.toString())
        }

    }
}