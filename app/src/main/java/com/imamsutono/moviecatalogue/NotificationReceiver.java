package com.imamsutono.moviecatalogue;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {
    public static String CHANNEL_ID = "channel_01";
    public static CharSequence CHANNEL_NAME = "movie catalogue";

    public static final String DAILY_REMINDER = "daily_reminder";
    public static final String EXTRA_MOVIE_ID = "extra_movie_id";

    private String title = "";
    private String content = "";
    private String subText = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        subText = context.getResources().getString(R.string.notif_sub_text);

        if (DAILY_REMINDER.equals(intent.getAction())) {
            title = context.getResources().getString(R.string.notif_daily_title);
            content = context.getResources().getString(R.string.notif_daily_content);
        } else {
            title = intent.getAction() + " is released today";
            content = intent.getAction() + " now available in Movie Catalogue";
        }

        showNotification(context, intent);
    }

    private void showNotification(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_movie_black_24dp)
                .setContentTitle(title)
                .setContentText(content)
                .setSubText(subText)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            builder.setChannelId(CHANNEL_ID);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        Notification notification = builder.build();

        if (notificationManager != null) {
            if (DAILY_REMINDER.equals(intent.getAction())) {
                notificationManager.notify(0, notification);
            } else {
                notificationManager.notify(intent.getIntExtra(EXTRA_MOVIE_ID, 1), notification);
            }
        }
    }
}
