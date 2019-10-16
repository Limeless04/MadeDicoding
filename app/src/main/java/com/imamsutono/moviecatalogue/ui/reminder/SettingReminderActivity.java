package com.imamsutono.moviecatalogue.ui.reminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.imamsutono.moviecatalogue.NotificationReceiver;
import com.imamsutono.moviecatalogue.R;
import com.imamsutono.moviecatalogue.ui.main.MainActivity;

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
        final MainActivity mainActivity = new MainActivity();

        if (releasePref.getBoolean(RELEASE_PREFERENCE, false))
            releaseSwitch.setChecked(true);

        releaseSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mainActivity.setupReleaseTodayReminder(getApplicationContext());
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
}
