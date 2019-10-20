package com.imamsutono.moviecatalogue.ui.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.imamsutono.moviecatalogue.NotificationReceiver;
import com.imamsutono.moviecatalogue.R;

import java.util.Calendar;

public class SettingReminderActivity extends AppCompatActivity {

    private NotificationReceiver notificationReceiver;
    public static final String DAILY_PREFERENCE = "dailyPreference";
    private final String RELEASE_PREFERENCE = "releasePreference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_reminder);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.reminder_setting));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        notificationReceiver = new NotificationReceiver();
        final SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                DAILY_PREFERENCE, Context.MODE_PRIVATE
        );
        Switch dailySwitch = findViewById(R.id.switch_daily);

        if (sharedPref.getBoolean(DAILY_PREFERENCE, false))
            dailySwitch.setChecked(true);

        dailySwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    notificationReceiver.setupDailyReminder(getApplicationContext());
                    sharedPref.edit().putBoolean(DAILY_PREFERENCE, true).apply();
                } else {
                    notificationReceiver.cancelReminder(getApplicationContext(), NotificationReceiver.DAILY_REMINDER);
                    sharedPref.edit().putBoolean(DAILY_PREFERENCE, false).apply();
                }
            }
        });

        final SharedPreferences releasePref = getApplicationContext().getSharedPreferences(
                RELEASE_PREFERENCE, Context.MODE_PRIVATE
        );
        Switch releaseSwitch = findViewById(R.id.switch_release);

        if (releasePref.getBoolean(RELEASE_PREFERENCE, false))
            releaseSwitch.setChecked(true);

        releaseSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    setupReleaseTodayReminder(getApplicationContext());
                    releasePref.edit().putBoolean(RELEASE_PREFERENCE, true).apply();
                } else {
                    notificationReceiver.cancelReminder(getApplicationContext(), NotificationReceiver.RELEASE_REMINDER);
                    releasePref.edit().putBoolean(RELEASE_PREFERENCE, false).apply();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void setupReleaseTodayReminder(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);

        final SharedPreferences releasePref = context.getSharedPreferences(
                RELEASE_PREFERENCE, Context.MODE_PRIVATE
        );

        if (alarmManager != null && !releasePref.getBoolean(RELEASE_PREFERENCE, false)) {
            Intent intent = new Intent(context, NotificationReceiver.class);
            intent.setAction(NotificationReceiver.RELEASE_REMINDER);
            intent.putExtra(NotificationReceiver.EXTRA_MOVIE_ID, 0);

            PendingIntent alarmIntent = PendingIntent.getBroadcast(getApplicationContext(),
                    NotificationReceiver.ID_RELEASE_REMINDER, intent, 0);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, alarmIntent);
        }

        Toast.makeText(context, R.string.release_reminder_active, Toast.LENGTH_SHORT).show();
    }
}
