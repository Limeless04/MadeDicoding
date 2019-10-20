package com.imamsutono.moviefavorite.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.imamsutono.moviefavorite.R;
import com.imamsutono.moviefavorite.model.Movie;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_FAVORITE = "extra_favorite";

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

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        progressBar = findViewById(R.id.loading_detail);
        imgPoster = findViewById(R.id.img_poster_detail);
        tvTitle = findViewById(R.id.txt_title_detail);
        tvYear = findViewById(R.id.txt_year_detail);
        tvVoters = findViewById(R.id.txt_voters_detail);
        tvScore = findViewById(R.id.txt_score_detail);
        tvDescription = findViewById(R.id.txt_description_detail);

        if (savedInstanceState == null) {
            initMovieData();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
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

    private void initMovieData() {
        Movie movie = getIntent().getParcelableExtra(EXTRA_FAVORITE);

        if (movie != null) {
            poster = movie.getPoster();
            title = movie.getTitle();
            year = movie.getYear();
            voters = movie.getVoters();
            score = movie.getScore();
            description = movie.getDescription();
            showDetail();
        }
    }

    private void showDetail() {
        Glide.with(this)
                .load("https://image.tmdb.org/t/p/original" + poster)
                .placeholder(new ColorDrawable(Color.GRAY))
                .error(new ColorDrawable(Color.GRAY))
                .transform(new CenterCrop(), new RoundedCorners(16))
                .into(imgPoster);
        tvTitle.setText(title);
        tvYear.setText(year);
        tvVoters.setText(voters);
        tvScore.setText(score);
        tvDescription.setText(description);
        progressBar.setVisibility(View.GONE);
    }
}
