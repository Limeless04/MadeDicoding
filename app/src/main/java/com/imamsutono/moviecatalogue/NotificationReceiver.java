package com.imamsutono.moviecatalogue;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.imamsutono.moviecatalogue.model.Movie;
import com.imamsutono.moviecatalogue.model.MovieResponse;
import com.imamsutono.moviecatalogue.service.ServiceGenerator;
import com.imamsutono.moviecatalogue.service.ServiceInterface;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationReceiver extends BroadcastReceiver {
    public static String CHANNEL_ID = "channel_01";
    public static CharSequence CHANNEL_NAME = "movie catalogue";

    public static final String DAILY_REMINDER = "daily_reminder";
    public static final String RELEASE_REMINDER = "release_reminder";
    public static final String EXTRA_MOVIE_ID = "extra_movie_id";
    public static final int ID_DAILY_REMINDER = 0;
    public static final int ID_RELEASE_REMINDER = 100;

    private String title = "";
    private String content = "";
    private String subText = "";

    private ServiceInterface service = ServiceGenerator.createService(ServiceInterface.class);
    private Call<MovieResponse> callMovie;

    @Override
    public void onReceive(Context context, Intent intent) {
        subText = context.getResources().getString(R.string.notif_sub_text);

        if (DAILY_REMINDER.equals(intent.getAction())) {
            title = context.getResources().getString(R.string.notif_daily_title);
            content = context.getResources().getString(R.string.notif_daily_content);
            showNotification(context, ID_DAILY_REMINDER);
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String now = dateFormat.format(new Date());
            callMovie = service.getTodayReleaseMovie(now, now);
            getTodayReleaseMovie(context);
        }
    }

    private void getTodayReleaseMovie(final Context context) {
        callMovie.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if (response.body() != null) {
                    List<Movie> movies = response.body().getMovies();

                    for (int i = 0; i < movies.size(); i++) {
                        title = movies.get(i).getTitle() + " is released today";
                        content = movies.get(i).getTitle() + " now available in Movie Catalogue";
                        showNotification(context, i);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                Log.e(NotificationReceiver.class.getSimpleName(), "Gagal mengambil data rilis hari ini");
            }
        });
    }

    public void setupDailyReminder(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.setAction(DAILY_REMINDER);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, ID_DAILY_REMINDER, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 0);

        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, alarmIntent);
        }

        Toast.makeText(context, R.string.daily_reminder_active, Toast.LENGTH_SHORT).show();
    }

    public void cancelReminder(Context context, String type) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationReceiver.class);
        int requestCode = type.equalsIgnoreCase(DAILY_REMINDER) ? ID_DAILY_REMINDER : ID_RELEASE_REMINDER;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        pendingIntent.cancel();

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }

        Toast.makeText(context, R.string.reminder_canceled, Toast.LENGTH_SHORT).show();
    }

    private void showNotification(Context context, int id) {
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
            notificationManager.notify(id, notification);
        }
    }
}
