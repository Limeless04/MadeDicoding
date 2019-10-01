package com.imamsutono.moviecatalogue.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.imamsutono.moviecatalogue.R;
import com.imamsutono.moviecatalogue.fragment.MainFragment;
import com.imamsutono.moviecatalogue.ui.favorite.FavoriteFragment;

import static com.imamsutono.moviecatalogue.fragment.MainFragment.ARG_OBJECT;

public class MainActivity extends AppCompatActivity {

    final Fragment mainFragment = new MainFragment();
    final Fragment favFragment = new FavoriteFragment();
    final FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        navView.setSelectedItemId(R.id.navigation_movie);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Bundle args = new Bundle();

            switch (menuItem.getItemId()) {
                case R.id.navigation_movie:
                    args.putString(ARG_OBJECT, "movie");
                    mainFragment.setArguments(args);
                    fm.beginTransaction().replace(R.id.main_content, mainFragment).commit();
                    return true;
                case R.id.navigation_tvshow:
                    args.putString(ARG_OBJECT, "tv_show");
                    mainFragment.setArguments(args);
                    fm.beginTransaction().replace(R.id.main_content, mainFragment).commit();
                    return true;
                default:
                    fm.beginTransaction().replace(R.id.main_content, favFragment).commit();
                    return true;
            }
        }
    };
}
