package com.imamsutono.moviecatalogue.ui.search;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.imamsutono.moviecatalogue.R;
import com.imamsutono.moviecatalogue.adapter.PagerAdapter;
import com.imamsutono.moviecatalogue.adapter.SearchPagerAdapter;

import java.util.Objects;

public class SearchFragment extends Fragment {

    private SearchViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);

        SearchPagerAdapter pagerAdapter = new SearchPagerAdapter(getChildFragmentManager());
        ViewPager viewPager = view.findViewById(R.id.viewpager_search);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = view.findViewById(R.id.tab_search);
        tabLayout.setupWithViewPager(viewPager);
        Objects.requireNonNull(tabLayout.getTabAt(0)).setText(R.string.tab_text_1);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setText(R.string.tab_text_2);

        setHasOptionsMenu(true);
        return view;
    }
}
