package com.imamsutono.moviecatalogue.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.imamsutono.moviecatalogue.ui.movie.MovieListFragment;
import com.imamsutono.moviecatalogue.ui.tvshow.TvShowListFragment;

public class SearchPagerAdapter extends FragmentPagerAdapter {

    public SearchPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new Fragment();

        switch (position) {
            case 0:
                fragment = new MovieListFragment();
                break;
            case 1:
                fragment = new TvShowListFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
