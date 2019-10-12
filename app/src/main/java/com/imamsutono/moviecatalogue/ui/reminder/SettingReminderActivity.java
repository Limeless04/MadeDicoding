package com.imamsutono.moviecatalogue.ui.reminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.imamsutono.moviecatalogue.NotificationReceiver;
import com.imamsutono.moviecatalogue.R;
import com.imamsutono.moviecatalogue.ui.main.MainActivity;

public class SettingReminderActivity extends AppCompatActivity {

    private NotificationReceiver notificationReceiver;

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

        Switch dailySwitch = findViewById(R.id.switch_daily);
        boolean dailyReminderIsSet = (PendingIntent.getBroadcast(getApplicationContext(),
                NotificationReceiver.ID_DAILY_REMINDER,
                new Intent(NotificationReceiver.DAILY_REMINDER),
                PendingIntent.FLAG_NO_CREATE) != null);

        if (dailyReminderIsSet)
            dailySwitch.setChecked(true);

        dailySwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    notificationReceiver.setupDailyReminder(getApplicationContext());
                } else {
                    notificationReceiver.cancelReminder(getApplicationContext(), NotificationReceiver.DAILY_REMINDER);
                }
            }
        });

        Switch releaseSwitch = findViewById(R.id.switch_release);
        final MainActivity mainActivity = new MainActivity();

        if (mainActivity.isReleaseReminderSet(getApplicationContext()))
            releaseSwitch.setChecked(true);

        releaseSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mainActivity.setupReleaseTodayReminder(getApplicationContext());
                } else {
                    notificationReceiver.cancelReminder(getApplicationContext(), NotificationReceiver.RELEASE_REMINDER);
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
