package io.wear.tether

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import io.wear.tether.messages.ConfigurationModel

private fun createNotification(context: Context, config: ConfigurationModel) {
    val disableIntent = Intent(context, javaClass<DisableHotspotActivity>())
    val disablePendingIntent = PendingIntent.getActivity(context, 0, disableIntent, 0)

    val action = NotificationCompat.Action
            .Builder(R.drawable.ic_portable_wifi_off_teal_800_36dp, null, disablePendingIntent)
            .build()

    val wearableOptions = NotificationCompat.WearableExtender()
            .addAction(action)
            .setContentAction(0)
            .setHintHideIcon(true)

    val configPageBuilder = NotificationCompat.Builder(context)
            .setContentTitle("Configuration")
            .setContentText("SSID: " + config.getSsid() + "\nPassword: " + config.getPassword())
            .extend(NotificationCompat.WearableExtender()
                    .setCustomSizePreset(Notification.WearableExtender.SIZE_LARGE))

    val builder = NotificationCompat.Builder(context)
            .setPriority(Notification.PRIORITY_MAX)
            .setContentTitle(context.getResources().getString(R.string.lb_wifi_tether_on))
            .setContentText("Swipe for details")
            .setSmallIcon(R.drawable.ic_portable_wifi_off_teal_800_36dp)
            .addAction(R.drawable.ic_portable_wifi_off_teal_800_36dp, "Turn off", disablePendingIntent)
            .setOngoing(true)
            .extend(wearableOptions.addPage(configPageBuilder.build()))


    val notificationId = 1
    val notificationManager = NotificationManagerCompat.from(context)
    val notification = builder.build()
    notificationManager.notify(notificationId, notification)
}