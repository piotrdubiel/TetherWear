package io.wear.tether;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

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
    public void onSuccess() {
        showSuccess(getResources().getString(R.string.lb_wifi_tether_on));
        createNotification();
    }

    private void createNotification() {
        Intent disableIntent = new Intent(this, DisableHotspotActivity.class);
        PendingIntent disablePendingIntent =
                PendingIntent.getActivity(this, 0, disableIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(getResources().getString(R.string.lb_wifi_tether_on))
                .setSmallIcon(R.drawable.icon_tether_enabled)
                .addAction(R.drawable.ic_close_white_48dp, "Turn off", disablePendingIntent)
                .addAction(R.drawable.ic_result_open_white, "Open on phone", disablePendingIntent);

        int notificationId = 1;
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        Notification notification = builder.build();
        notificationManager.notify(notificationId, notification);
    }
}

