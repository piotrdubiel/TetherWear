package io.wear.tether

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat

import io.wear.tether.messages.ConfigurationModel
import io.wear.tether.state.DisconnectedState

import io.wear.tether.messages.MessageConstants.REQUEST_WIFI_TETHER_ON

public class EnableHotspotActivity : BaseHotspotActivity(REQUEST_WIFI_TETHER_ON) {

    companion object {
        public fun intent(context: Context): Intent = Intent(context, javaClass<EnableHotspotActivity>())
    }

    override fun onStart() {
        super.onStart()
        setState(DisconnectedState())
    }

    override fun onSuccess(config: ConfigurationModel?) {
        showSuccess(getResources().getString(R.string.lb_wifi_tether_on))
        createNotification(config!!)
    }

    private fun createNotification(config: ConfigurationModel) {
        createNotification(this, config)
    }
}

