package io.wear.tether.state;

import android.app.Notification;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import io.wear.tether.R;
import io.wear.tether.WifiTetherActivity;

public class SuccessState extends WifiTetherActivityState {
    @Override
    public void onStateApplied(WifiTetherActivity stateContext) {
        super.onStateApplied(stateContext);
        stateContext.showSuccess(stateContext.getResources().getString(R.string.lb_wifi_tether_on));
        createNotification();
        stateContext.finish();
    }

    private void createNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(stateContext)
                .setSmallIcon(R.drawable.action_wifi_tethering)
                .setContentTitle("A")
                .setContentText("B");

        NotificationCompat.Action action = new NotificationCompat.Action.Builder(
                R.drawable.action_wifi_tethering,
                "Action",
                null).build();

        NotificationCompat.WearableExtender wearableOptions =
                new NotificationCompat.WearableExtender()
                        .addAction(action)
                        .setContentAction(0)
                        .setHintHideIcon(true);
        builder.extend(wearableOptions);


        int notificationId = 1;
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(stateContext);
        notificationManager.notify(notificationId, builder.build());
    }
}
