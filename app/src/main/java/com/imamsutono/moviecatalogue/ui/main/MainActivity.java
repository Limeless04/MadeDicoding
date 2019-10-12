package com.imamsutono.moviecatalogue.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.imamsutono.moviecatalogue.NotificationReceiver;
import com.imamsutono.moviecatalogue.R;
import com.imamsutono.moviecatalogue.fragment.MainFragment;
import com.imamsutono.moviecatalogue.model.Movie;
import com.imamsutono.moviecatalogue.model.MovieResponse;
import com.imamsutono.moviecatalogue.ui.favorite.FavoriteFragment;
import com.imamsutono.moviecatalogue.ui.search.SearchFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.imamsutono.moviecatalogue.fragment.MainFragment.ARG_OBJECT;

public class MainActivity extends AppCompatActivity {
    final Fragment favFragment = new FavoriteFragment();
    final Fragment searchFragment = new SearchFragment();
    final FragmentManager fm = getSupportFragmentManager();
    private List<Movie> todayReleaseMovie = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        if (savedInstanceState == null) {
            navView.setSelectedItemId(R.id.navigation_movie);
        }

        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String now = dateFormat.format(new Date());

        mainViewModel.init(now);
        mainViewModel.getTodayReleaseMovie().observe(this, getTodayReleaseMovie);
    }

    public void setupReleaseTodayReminder(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 8);

        if (alarmManager != null && !isReleaseReminderSet(context)) {
            for (int i = 0; i < todayReleaseMovie.size(); i++) {
                Intent intent = new Intent(context, NotificationReceiver.class);
                intent.setAction(todayReleaseMovie.get(i).getTitle());
                intent.putExtra(NotificationReceiver.EXTRA_MOVIE_ID, i);

                PendingIntent alarmIntent = PendingIntent.getBroadcast(getApplicationContext(),
                        NotificationReceiver.ID_RELEASE_REMINDER, intent, 0);
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, alarmIntent);
            }
        }

        Toast.makeText(context, R.string.release_reminder_active, Toast.LENGTH_SHORT).show();
    }

    public boolean isReleaseReminderSet(Context context) {
        if (todayReleaseMovie.size() > 0) {
            return (PendingIntent.getBroadcast(context,
                    NotificationReceiver.ID_RELEASE_REMINDER,
                    new Intent(todayReleaseMovie.get(0).getTitle()),
                    PendingIntent.FLAG_NO_CREATE) != null);
        } else {
            return false;
        }
    }

    private Observer<MovieResponse> getTodayReleaseMovie = new Observer<MovieResponse>() {
        @Override
        public void onChanged(MovieResponse movieResponse) {
            if (movieResponse != null) {
                List<Movie> movies = movieResponse.getMovies();
                todayReleaseMovie.addAll(movies);
//                setupReleaseTodayReminder();
            } else {
                Toast.makeText(getApplicationContext(), "Gagal mengambil data rilis hari ini", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment mainFragment = new MainFragment();
            Bundle args = new Bundle();

            switch (menuItem.getItemId()) {
                case R.id.navigation_movie:
                    args.putString(ARG_OBJECT, "movies");
                    mainFragment.setArguments(args);
                    fm.beginTransaction().replace(R.id.main_content, mainFragment).commit();
                    return true;
                case R.id.navigation_tvshow:
                    args.putString(ARG_OBJECT, "tv_show");
                    mainFragment.setArguments(args);
                    fm.beginTransaction().replace(R.id.main_content, mainFragment).commit();
                    return true;
                case R.id.navigation_search:
                    fm.beginTransaction().replace(R.id.main_content, searchFragment).commit();
                    return true;
                default:
                    fm.beginTransaction().replace(R.id.main_content, favFragment).commit();
                    return true;
            }
        }
    };
}
