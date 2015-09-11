package io.wear.tether

import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationManagerCompat

import io.wear.tether.messages.ConfigurationModel
import io.wear.tether.state.DisconnectedState

import io.wear.tether.messages.MessageConstants.REQUEST_WIFI_TETHER_OFF

public class DisableHotspotActivity : BaseHotspotActivity(REQUEST_WIFI_TETHER_OFF) {

    companion object {
        public fun intent(context: Context): Intent = Intent(context, javaClass<DisableHotspotActivity>())
    }

    override fun onStart() {
        super.onStart()
        setState(DisconnectedState())
    }

    override fun onSuccess(config: ConfigurationModel?) {
        showSuccess(getResources().getString(R.string.lb_wifi_tether_off))
        NotificationManagerCompat.from(this).cancelAll()
    }
}
