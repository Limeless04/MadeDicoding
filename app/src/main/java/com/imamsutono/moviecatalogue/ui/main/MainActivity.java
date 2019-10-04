package com.imamsutono.moviecatalogue.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.imamsutono.moviecatalogue.R;
import com.imamsutono.moviecatalogue.fragment.MainFragment;
import com.imamsutono.moviecatalogue.ui.favorite.FavoriteFragment;
import com.imamsutono.moviecatalogue.ui.search.SearchFragment;

import static com.imamsutono.moviecatalogue.fragment.MainFragment.ARG_OBJECT;

public class MainActivity extends AppCompatActivity {

    final Fragment favFragment = new FavoriteFragment();
    final Fragment searchFragment = new SearchFragment();
    final FragmentManager fm = getSupportFragmentManager();

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
        mainViewModel.init();
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
