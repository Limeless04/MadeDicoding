package com.imamsutono.moviefavorite.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.imamsutono.moviefavorite.LoadFavoritesCallback;
import com.imamsutono.moviefavorite.R;
import com.imamsutono.moviefavorite.adapter.MovieAdapter;
import com.imamsutono.moviefavorite.helper.MappingHelper;
import com.imamsutono.moviefavorite.model.Movie;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.imamsutono.moviefavorite.db.DatabaseContract.DbColumns.CONTENT_URI;

public class MainActivity extends AppCompatActivity implements LoadFavoritesCallback {

    private ProgressBar progressBar;
    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Movie Favorite");
        }
        progressBar = findViewById(R.id.loading);

        RecyclerView rvFavorite = findViewById(R.id.recycler_view);
        rvFavorite.setHasFixedSize(true);
        movieAdapter = new MovieAdapter();
        rvFavorite.setAdapter(movieAdapter);
        movieAdapter.setOnItemClickCallback(new MovieAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Movie data) {
                openMovieDetail(data);
            }
        });

        openDatabase();
        new LoadMoviesAsync(getApplicationContext(), this).execute();
    }

    private void openMovieDetail(Movie data) {
        Movie movie = new Movie();
        movie.setId(data.getId());
        movie.setPoster(data.getPoster());
        movie.setTitle(data.getTitle());
        movie.setYear(data.getYear());
        movie.setVoters(data.getVoters());
        movie.setScore(data.getScore());
        movie.setDescription(data.getDescription());

        Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_FAVORITE, movie);
        startActivity(intent);
    }

    private void openDatabase() {
        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());

        DataObserver dataObserver = new DataObserver(handler, getApplicationContext());
        getApplicationContext().getContentResolver().registerContentObserver(CONTENT_URI, true, dataObserver);
    }

    @Override
    public void preExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void postExecute(List<Movie> movies) {
        progressBar.setVisibility(View.INVISIBLE);

        if (movies.size() > 0) {
            movieAdapter.setData(movies);
        } else {
            movieAdapter.setData(new ArrayList<Movie>());
            Toast.makeText(this, "Tidak ada data film", Toast.LENGTH_SHORT).show();
        }
    }

    private static class LoadMoviesAsync extends AsyncTask<Void, Void, List<Movie>> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadFavoritesCallback> weakCallback;

        private LoadMoviesAsync(Context context, LoadFavoritesCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected List<Movie> doInBackground(Void... voids) {
            Context context = weakContext.get();
            Cursor dataCursor = context.getContentResolver().query(CONTENT_URI, null, null, null, null);
            return MappingHelper.mapCursorToList(dataCursor);
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);
            weakCallback.get().postExecute(movies);
        }
    }

    public static class DataObserver extends ContentObserver {

        final Context context;

        DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadMoviesAsync(context, (LoadFavoritesCallback) context).execute();
        }
    }
}
