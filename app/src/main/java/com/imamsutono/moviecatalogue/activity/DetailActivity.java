package com.imamsutono.moviecatalogue.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.imamsutono.moviecatalogue.service.ServiceInterface;
import com.imamsutono.moviecatalogue.service.ServiceGenerator;
import com.imamsutono.moviecatalogue.model.Movie;
import com.imamsutono.moviecatalogue.R;
import com.imamsutono.moviecatalogue.model.TvShow;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_TYPE = "extra_data";
    private Movie movie;
    private TvShow tvShow;

    private ServiceInterface service = ServiceGenerator.createService(ServiceInterface.class);
    private Call<Movie> callMovie;
    private Call<TvShow> callTvShow;
    String type;

    ProgressBar progressBar;
    ImageView imgPoster;
    TextView tvTitle;
    TextView tvYear;
    TextView tvVoters;
    TextView tvScore;
    TextView tvDescription;

    String poster = "";
    String title = "";
    String year = "";
    String voters = "";
    String score = "";
    String description = "";

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

        type = getIntent().getStringExtra(EXTRA_TYPE);

        Bundle extras = getIntent().getExtras();
        int id = extras.getInt(EXTRA_ID, 2);
        callMovie = service.getMovieDetail(id);
        callTvShow = service.getTvShowDetai(id);

        if (savedInstanceState == null) {

            if (type.equals("movie")) {
                getMovieDetail();
            } else {
                getTvShowDetail();
            }
        }
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

    private void getMovieDetail() {
        callMovie.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                movie = response.body();

                poster += movie.getPoster();
                title = movie.getTitle();
                year = movie.getYear();
                voters = movie.getVoters();
                score = movie.getScore() + "%";
                description = movie.getDescription();

                showDetail();
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Gagal mengambil data detail film", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTvShowDetail() {
        callTvShow.enqueue(new Callback<TvShow>() {
            @Override
            public void onResponse(Call<TvShow> call, Response<TvShow> response) {
                tvShow = response.body();

                poster += tvShow.getPoster();
                title = tvShow.getTitle();
                year = tvShow.getYear();
                voters = tvShow.getVoters();
                score = tvShow.getScore() + "%";
                description = tvShow.getDescription();

                showDetail();
            }

            @Override
            public void onFailure(Call<TvShow> call, Throwable t) {
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
}
