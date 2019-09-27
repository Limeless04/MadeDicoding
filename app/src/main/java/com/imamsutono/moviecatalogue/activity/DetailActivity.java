package com.imamsutono.moviecatalogue.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.imamsutono.moviecatalogue.db.MovieHelper;
import com.imamsutono.moviecatalogue.service.ServiceInterface;
import com.imamsutono.moviecatalogue.service.ServiceGenerator;
import com.imamsutono.moviecatalogue.model.Movie;
import com.imamsutono.moviecatalogue.R;
import com.imamsutono.moviecatalogue.model.TvShow;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_TYPE = "extra_data";
    public static final String EXTRA_MOVIE = "extra_movie";
    private Movie movie;
    private int position;
    public static final String EXTRA_POSITION = "extra_position";
    public static final int RESULT_ADD = 101;
    public static final int RESULT_DELETE = 301;
    private MovieHelper movieHelper;

    private TvShow tvShow;

    private ServiceInterface service = ServiceGenerator.createService(ServiceInterface.class);
    private Call<Movie> callMovie;
    private Call<TvShow> callTvShow;

    private ProgressBar progressBar;
    private ImageView imgPoster;
    private TextView tvTitle;
    private TextView tvYear;
    private TextView tvVoters;
    private TextView tvScore;
    private TextView tvDescription;
    private FloatingActionButton btnFavorite;

    private String poster = "";
    private String title = "";
    private String year = "";
    private String voters = "";
    private String score = "";
    private String description = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        progressBar = findViewById(R.id.loading_detail);
        imgPoster = findViewById(R.id.img_poster_detail);
        tvTitle = findViewById(R.id.txt_title_detail);
        tvYear = findViewById(R.id.txt_year_detail);
        tvVoters = findViewById(R.id.txt_voters_detail);
        tvScore = findViewById(R.id.txt_score_detail);
        tvDescription = findViewById(R.id.txt_description_detail);

        movieHelper = MovieHelper.getInstance(getApplicationContext());
        movieHelper.open();

        String type = getIntent().getStringExtra(EXTRA_TYPE);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            int id = extras.getInt(EXTRA_ID, 2);
            callMovie = service.getMovieDetail(id);
            callTvShow = service.getTvShowDetai(id);
        }

        movie = getIntent().getParcelableExtra(EXTRA_MOVIE);

        if (movie != null) {
            position = getIntent().getIntExtra(EXTRA_POSITION, 0);
        }

        if (savedInstanceState == null && type != null) {
            if (type.equals("movie")) {
                getMovieDetail();
            } else {
                getTvShowDetail();
            }
        }

        btnFavorite = findViewById(R.id.btn_favorite);
        btnFavorite.setOnClickListener(this);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("poster", poster);
        outState.putString("title", title);
        outState.putString("year", year);
        outState.putString("voters", voters);
        outState.putString("score", score);
        outState.putString("description", description);
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        poster = savedInstanceState.getString("poster");
        title = savedInstanceState.getString("title");
        year = savedInstanceState.getString("year");
        voters = savedInstanceState.getString("voters");
        score = savedInstanceState.getString("score");
        description = savedInstanceState.getString("description");

        showDetail();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        movieHelper.close();
    }

    private void getMovieDetail() {
        callMovie.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                movie = response.body();

                if (movie != null) {
                    poster += movie.getPoster();
                    title = movie.getTitle();
                    year = movie.getYear();
                    voters = movie.getVoters();
                    score = movie.getScore() + "%";
                    description = movie.getDescription();
                    showDetail();
                    setBtnFavoriteIcon();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                Toast.makeText(DetailActivity.this, "Gagal mengambil data detail film", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTvShowDetail() {
        callTvShow.enqueue(new Callback<TvShow>() {
            @Override
            public void onResponse(@NonNull Call<TvShow> call, @NonNull Response<TvShow> response) {
                tvShow = response.body();

                if (tvShow != null) {
                    poster += tvShow.getPoster();
                    title = tvShow.getTitle();
                    year = tvShow.getYear();
                    voters = tvShow.getVoters();
                    score = tvShow.getScore() + "%";
                    description = tvShow.getDescription();
                    showDetail();
                }
            }

            @Override
            public void onFailure(@NonNull Call<TvShow> call, @NonNull Throwable t) {
                Toast.makeText(DetailActivity.this, "Gagal mengambil data detail pertunjukan TV", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDetail() {
        Glide.with(this)
                .load(poster)
                .placeholder(new ColorDrawable(Color.GRAY))
                .error(new ColorDrawable(Color.GRAY))
                .transform(new CenterCrop(), new RoundedCorners(16))
                .into(imgPoster);
        tvTitle.setText(title);
        tvYear.setText(year);
        tvVoters.setText(voters);
        tvScore.setText(score);
        tvDescription.setText(description);

        hideLoading();
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    private void setBtnFavoriteIcon() {
        boolean isFavorited = movieHelper.getMovie(title, year) > 0;
        int icon;

        if (isFavorited) {
            icon = R.drawable.ic_favorite_black_24dp;
        } else {
            icon = R.drawable.ic_favorite_border_black_24dp;
        }

        btnFavorite.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(), icon)
        );
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_favorite) {
            movie.setPoster(poster);
            movie.setTitle(title);
            movie.setYear(year);
            movie.setLanguage("en");

            Intent intent = new Intent();
            intent.putExtra(EXTRA_MOVIE, movie);
            intent.putExtra(EXTRA_POSITION, position);
            boolean isFavorited = movieHelper.getMovie(title, year) > 0;
            String message;

            if (!isFavorited) {
                long result = movieHelper.insertMovie(movie);

                if (result > 0) {
                    movie.setId((int) result);
                    setResult(RESULT_ADD, intent);
//                    btnFavorite.setImageDrawable(
//                            ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_black_24dp)
//                    );
                    setBtnFavoriteIcon();
                    message = "Berhasil ditambahkan ke favorit";
                } else {
                    message = "Gagal menambahkan favorit";
                }
            } else {
                long delete = movieHelper.deleteMovie(title, year);

                if (delete > 0) {
                    Intent delIntent = new Intent();
                    delIntent.putExtra(EXTRA_POSITION, position);
                    setResult(RESULT_DELETE, delIntent);
//                    btnFavorite.setImageDrawable(
//                            ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_border_black_24dp)
//                    );
                    setBtnFavoriteIcon();
                    message = "Berhasil dihapus dari favorit";
                } else {
                    message = "Gagal menghapus favorit";
                }
            }

            Toast.makeText(DetailActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    }
}
