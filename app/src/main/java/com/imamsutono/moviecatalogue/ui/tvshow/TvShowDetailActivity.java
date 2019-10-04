package com.imamsutono.moviecatalogue.ui.tvshow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.imamsutono.moviecatalogue.R;
import com.imamsutono.moviecatalogue.activity.DetailActivity;
import com.imamsutono.moviecatalogue.db.TvShowHelper;
import com.imamsutono.moviecatalogue.model.TvShow;
import com.imamsutono.moviecatalogue.service.ServiceGenerator;
import com.imamsutono.moviecatalogue.service.ServiceInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvShowDetailActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_TVSHOW = "extra_tvshow";
    public static final String EXTRA_POSITION = "extra_position";

    private TvShow tvShow;
    private TvShowHelper tvShowHelper;
    private ServiceInterface service = ServiceGenerator.createService(ServiceInterface.class);
    private Call<TvShow> callTvShow;
    private int position;

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
        btnFavorite = findViewById(R.id.btn_favorite);
        btnFavorite.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            int id = extras.getInt(EXTRA_ID, 2);
            callTvShow = service.getTvShowDetail(id);
        }

        tvShowHelper = TvShowHelper.getInstance(getApplicationContext());
        tvShowHelper.open();
        tvShow = getIntent().getParcelableExtra(EXTRA_TVSHOW);
        position = getIntent().getIntExtra(EXTRA_POSITION, 0);

        getTvShowDetail();
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

        getTvShowDetail();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tvShowHelper.close();
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
                Toast.makeText(TvShowDetailActivity.this, "Gagal mengambil data detail pertunjukan TV", Toast.LENGTH_SHORT).show();
            }
        });
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

        hideLoading();
        setBtnFavoriteIcon();
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    private void setBtnFavoriteIcon() {
        boolean isFavorited = tvShowHelper.getTvShow(title, year) > 0;
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
            Intent intent = new Intent();
            boolean isFavorited;

            tvShow.setPoster(poster);
            tvShow.setTitle(title);
            tvShow.setYear(year);
            tvShow.setLanguage("en");

            intent.putExtra(EXTRA_TVSHOW, tvShow);
            isFavorited = tvShowHelper.getTvShow(title, year) > 0;

            intent.putExtra(EXTRA_POSITION, position);
            String message;

            if (!isFavorited) {
                long result = tvShowHelper.insertTvShow(tvShow);

                if (result > 0) {
                    tvShow.setId((int) result);
                    setBtnFavoriteIcon();
                    message = "Berhasil ditambahkan ke favorit";
                } else {
                    message = "Gagal menambahkan favorit";
                }
            } else {
                long delete = tvShowHelper.deleteTvShow(title, year);

                if (delete > 0) {
                    Intent delIntent = new Intent();
                    delIntent.putExtra(EXTRA_POSITION, position);
                    setBtnFavoriteIcon();
                    message = "Berhasil dihapus dari favorit";
                } else {
                    message = "Gagal menghapus favorit";
                }
            }

            Toast.makeText(TvShowDetailActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    }
}
