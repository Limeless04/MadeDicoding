package com.imamsutono.moviecatalogue.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.imamsutono.moviecatalogue.NotificationReceiver;
import com.imamsutono.moviecatalogue.R;
import com.imamsutono.moviecatalogue.activity.DetailActivity;
import com.imamsutono.moviecatalogue.adapter.MovieAdapter;
import com.imamsutono.moviecatalogue.adapter.TvShowAdapter;
import com.imamsutono.moviecatalogue.model.Movie;
import com.imamsutono.moviecatalogue.model.MovieResponse;
import com.imamsutono.moviecatalogue.model.TvShow;
import com.imamsutono.moviecatalogue.model.TvShowResponse;
import com.imamsutono.moviecatalogue.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    private RecyclerView rvMovies;
    private ProgressBar progressBar;

    private List<Movie> listMovie = new ArrayList<>();
    private List<TvShow> listTvShow = new ArrayList<>();
    public static final String ARG_OBJECT = "ARG_OBJECT";
    private MainViewModel mainViewModel;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        setHasOptionsMenu(true);

        rvMovies = view.findViewById(R.id.recyclerview);
        rvMovies.setHasFixedSize(true);
        progressBar = view.findViewById(R.id.loading);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.init();

        Bundle args = getArguments();
        if (args != null) initArray(args.getString(ARG_OBJECT));

        showLoading();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull  Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();

        switch (item.getItemId()) {
            case R.id.action_change_settings:
                intent.setAction(Settings.ACTION_LOCALE_SETTINGS);
                break;
            case R.id.notification_settings:
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, getContext().getPackageName());
                    Toast.makeText(getContext(), getContext().getPackageName(), Toast.LENGTH_SHORT).show();
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                    intent.putExtra("app_package", getContext().getPackageName());
                    intent.putExtra("app_uid", getContext().getApplicationInfo().uid);
                } else {
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setData(Uri.parse("package:" + getContext().getPackageName()));
                }
                break;
        }

        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    private void initArray(String tab) {
        if (listMovie.isEmpty() && listTvShow.isEmpty()) {
            if (tab.equals("movies")) {
                mainViewModel.getMovies().observe(this, getMovie);
            } else {
                mainViewModel.getTvShows().observe(this, getTvShow);
            }
        }
    }

    private Observer<MovieResponse> getMovie = new Observer<MovieResponse>() {
        @Override
        public void onChanged(MovieResponse movieReponse) {
            if (movieReponse != null) {
                List<Movie> movies = movieReponse.getMovies();
                listMovie.addAll(movies);
                showMovies();
            } else {
                Toast.makeText(getContext(), "Gagal mengambil data film", Toast.LENGTH_SHORT).show();
                hideLoading();
            }
        }
    };

    private void showMovies() {
        MovieAdapter movieAdapter = new MovieAdapter(listMovie);
        rvMovies.setAdapter(movieAdapter);
        movieAdapter.notifyDataSetChanged();

        movieAdapter.setOnItemClickCallback(new MovieAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Movie data) {
                openDetail("movie", data.getId());
            }
        });
        hideLoading();
    }

    private Observer<TvShowResponse> getTvShow = new Observer<TvShowResponse>() {
        @Override
        public void onChanged(TvShowResponse tvShowResponse) {
            if (tvShowResponse != null) {
                List<TvShow> tvShows = tvShowResponse.getResults();
                listTvShow.addAll(tvShows);
                showTvShows();
            } else {
                Toast.makeText(getContext(), "Gagal mengambil data pertunjukan TV", Toast.LENGTH_SHORT).show();
                hideLoading();
            }
        }
    };

    private void showTvShows() {
        TvShowAdapter tvShowAdapter = new TvShowAdapter(listTvShow);
        rvMovies.setAdapter(tvShowAdapter);
        tvShowAdapter.notifyDataSetChanged();

        tvShowAdapter.setOnItemClickCallback(new TvShowAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(TvShow data) {
                openDetail("tv_show", data.getId());
            }
        });
        hideLoading();
    }

    private void openDetail(String type, int id) {
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_ID, id);
        intent.putExtra(DetailActivity.EXTRA_TYPE, type);
        startActivity(intent);
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }
}
