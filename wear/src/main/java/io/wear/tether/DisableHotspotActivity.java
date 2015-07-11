package io.wear.tether;

import android.support.v4.app.NotificationManagerCompat;

import io.wear.tether.messages.ConfigurationModel;
import io.wear.tether.state.DisconnectedState;

import static io.wear.tether.messages.MessageConstants.REQUEST_WIFI_TETHER_OFF;

public class DisableHotspotActivity extends BaseHotspotActivity {
    public DisableHotspotActivity() {
        super(REQUEST_WIFI_TETHER_OFF);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setState(new DisconnectedState());
    }

    @Override
    public void onSuccess(ConfigurationModel config) {
        showSuccess(getResources().getString(R.string.lb_wifi_tether_off));
        NotificationManagerCompat.from(this).cancelAll();
    }
}
