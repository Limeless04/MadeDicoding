package com.imamsutono.moviecatalogue.ui.favoritelist;

import androidx.lifecycle.ViewModelProviders;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.Snackbar;
import com.imamsutono.moviecatalogue.R;
import com.imamsutono.moviecatalogue.adapter.MovieAdapter;
import com.imamsutono.moviecatalogue.adapter.TvShowAdapter;
import com.imamsutono.moviecatalogue.db.MovieHelper;
import com.imamsutono.moviecatalogue.db.TvShowHelper;
import com.imamsutono.moviecatalogue.model.Movie;
import com.imamsutono.moviecatalogue.model.TvShow;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.imamsutono.moviecatalogue.fragment.MainFragment.ARG_OBJECT;

public class FavoriteListFragment extends Fragment implements LoadFavoritesCallback {

    private RecyclerView rvFavorite;
    private ProgressBar progressBar;
    private FavoriteListViewModel mViewModel;
    private MovieHelper movieHelper;
    private TvShowHelper tvShowHelper;
    private MovieAdapter movieAdapter;
    private TvShowAdapter tvShowAdapter;

    private final String TYPE_MOVIES = "movies";
    private static final String EXTRA_STATE = "EXTRA_STATE";
    private String type;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favorite_list_fragment, container, false);
        progressBar = view.findViewById(R.id.progress_fav_list);
        rvFavorite = view.findViewById(R.id.rv_fav_list);
        rvFavorite.setHasFixedSize(true);

        mViewModel = ViewModelProviders.of(this).get(FavoriteListViewModel.class);
        mViewModel.init();

        Bundle args = getArguments();

        if (args != null) {
            type = args.getString(ARG_OBJECT);

            if (type != null) {
                if (type.equals(TYPE_MOVIES)) {
                    movieAdapter = new MovieAdapter();
                    rvFavorite.setAdapter(movieAdapter);
                } else {
                    tvShowAdapter = new TvShowAdapter();
                    rvFavorite.setAdapter(tvShowAdapter);
                }
                openDatabase();
            }
        }

        if (type != null) {
            if (savedInstanceState == null) {
                if (type.equals(TYPE_MOVIES)) {
                    new LoadMoviesAsync(movieHelper, this).execute();
                } else {
                    new LoadTvShowsAsync(tvShowHelper, this).execute();
                }
            } else {
                if (type.equals(TYPE_MOVIES)) {
                    List<Movie> movies = savedInstanceState.getParcelableArrayList(EXTRA_STATE);

                    if (movies != null)
                        movieAdapter.setData(movies);
                } else {
                    List<TvShow> tvShows = savedInstanceState.getParcelableArrayList(EXTRA_STATE);

                    if (tvShows != null)
                        tvShowAdapter.setData(tvShows);
                }
            }
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(FavoriteListViewModel.class);
    }

    private void openDatabase() {
        if (type.equals(TYPE_MOVIES)) {
            movieHelper = MovieHelper.getInstance(getContext());
            movieHelper.open();
        } else {
            tvShowHelper = TvShowHelper.getInstance(getContext());
            tvShowHelper.open();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (type.equals(TYPE_MOVIES)) {
            movieHelper.close();
        } else {
            tvShowHelper.close();
        }
    }

    @Override
    public void preExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void postExecuteMovie(List<Movie> movies) {
        progressBar.setVisibility(View.INVISIBLE);

        if (movies.size() > 0) {
            movieAdapter.setData(movies);
        } else {
            movieAdapter.setData(new ArrayList<Movie>());
            showSnackbar("Tidak ada data film");
        }
    }

    @Override
    public void postExecuteTvShow(List<TvShow> tvShows) {
        progressBar.setVisibility(View.INVISIBLE);

        if (tvShows.size() > 0) {
            tvShowAdapter.setData(tvShows);
        } else {
            tvShowAdapter.setData(new ArrayList<TvShow>());
            showSnackbar("Tidak ada data pertunjukan TV");
        }
    }

    private void showSnackbar(String message) {
        Snackbar.make(rvFavorite, message, Snackbar.LENGTH_SHORT).show();
    }

    private static class LoadMoviesAsync extends AsyncTask<Void, Void, List<Movie>> {
        private final WeakReference<MovieHelper> weakMovieHelper;
        private final WeakReference<LoadFavoritesCallback> weakCallback;

        private LoadMoviesAsync(MovieHelper movieHelper, LoadFavoritesCallback callback) {
            weakMovieHelper = new WeakReference<>(movieHelper);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected List<Movie> doInBackground(Void... voids) {
            return weakMovieHelper.get().getAllMovies();
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);
            weakCallback.get().postExecuteMovie(movies);
        }
    }

    private static class LoadTvShowsAsync extends AsyncTask<Void, Void, List<TvShow>> {
        private final WeakReference<TvShowHelper> weakTvShowHelper;
        private final WeakReference<LoadFavoritesCallback> weakCallback;

        private LoadTvShowsAsync(TvShowHelper tvShowHelper, LoadFavoritesCallback callback) {
            weakTvShowHelper = new WeakReference<>(tvShowHelper);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected List<TvShow> doInBackground(Void... voids) {
            return weakTvShowHelper.get().getAllTvShow();
        }

        @Override
        protected void onPostExecute(List<TvShow> tvShows) {
            super.onPostExecute(tvShows);
            weakCallback.get().postExecuteTvShow(tvShows);
        }
    }
}

interface LoadFavoritesCallback {
    void preExecute();
    void postExecuteMovie(List<Movie> movies);
    void postExecuteTvShow(List<TvShow> tvShows);
}
