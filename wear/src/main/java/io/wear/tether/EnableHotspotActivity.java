package io.wear.tether;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import io.wear.tether.messages.ConfigurationModel;
import io.wear.tether.state.DisconnectedState;

import static io.wear.tether.messages.MessageConstants.REQUEST_WIFI_TETHER_ON;

public class EnableHotspotActivity extends BaseHotspotActivity {
    public EnableHotspotActivity() {
        super(REQUEST_WIFI_TETHER_ON);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setState(new DisconnectedState());
    }


    @Override
    public void onSuccess(ConfigurationModel config) {
        showSuccess(getResources().getString(R.string.lb_wifi_tether_on));
        createNotification(config);
    }

    private void createNotification(ConfigurationModel config) {
        Intent disableIntent = new Intent(this, DisableHotspotActivity.class);
        PendingIntent disablePendingIntent =
                PendingIntent.getActivity(this, 0, disableIntent, 0);

        NotificationCompat.Action action = new NotificationCompat.Action.Builder(
                R.drawable.ic_portable_wifi_off_teal_800_36dp, null, disablePendingIntent).build();

        NotificationCompat.WearableExtender wearableOptions =
                new NotificationCompat.WearableExtender()
                        .addAction(action)
                        .setContentAction(0)
                        .setHintHideIcon(true);

        NotificationCompat.Builder configPageBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("Configuration")
                .setContentText("SSID: " + config.getSsid() + "\nPassword: " + config.getPassword())
                .extend(new NotificationCompat.WearableExtender()
                        .setCustomSizePreset(Notification.WearableExtender.SIZE_LARGE)
                );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(getResources().getString(R.string.lb_wifi_tether_on))
                .setContentText("Swipe for details")
                .setSmallIcon(R.drawable.ic_portable_wifi_off_teal_800_36dp)
                .addAction(R.drawable.ic_portable_wifi_off_teal_800_36dp, "Turn off", disablePendingIntent)
                .setOngoing(true)
                .extend(wearableOptions.addPage(configPageBuilder.build()));


        int notificationId = 1;
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        Notification notification = builder.build();
        notificationManager.notify(notificationId, notification);
    }
}

