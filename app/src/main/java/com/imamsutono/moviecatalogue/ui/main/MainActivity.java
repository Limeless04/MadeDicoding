package com.imamsutono.moviecatalogue.ui.main;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.imamsutono.moviecatalogue.R;
import com.imamsutono.moviecatalogue.fragment.MainFragment;
import com.imamsutono.moviecatalogue.model.Movie;
import com.imamsutono.moviecatalogue.ui.favorite.FavoriteFragment;
import com.imamsutono.moviecatalogue.ui.search.SearchFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    }

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
