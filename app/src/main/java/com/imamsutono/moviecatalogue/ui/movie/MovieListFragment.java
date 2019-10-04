package com.imamsutono.moviecatalogue.ui.movie;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.imamsutono.moviecatalogue.R;
import com.imamsutono.moviecatalogue.activity.DetailActivity;
import com.imamsutono.moviecatalogue.adapter.MovieAdapter;
import com.imamsutono.moviecatalogue.model.Movie;
import com.imamsutono.moviecatalogue.model.MovieResponse;
import com.imamsutono.moviecatalogue.viewmodel.MovieListViewModel;

import java.util.ArrayList;
import java.util.List;

public class MovieListFragment extends Fragment {

    private RecyclerView rvMovies;
    private ProgressBar progressBar;
    private List<Movie> listMovie = new ArrayList<>();

    public MovieListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);

        rvMovies = view.findViewById(R.id.rv_list);
        rvMovies.setHasFixedSize(true);
        progressBar = view.findViewById(R.id.progress_list);

        MovieListViewModel viewModel = ViewModelProviders.of(this).get(MovieListViewModel.class);
        viewModel.init();
        viewModel.getMovies().observe(this, getMovie);
        showLoading();

        return view;
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
                openDetail(data.getId());
            }
        });
        hideLoading();
    }

    private void openDetail(int id) {
        Intent intent = new Intent(getContext(), MovieDetailFragment.class);
        intent.putExtra(MovieDetailFragment.EXTRA_ID, id);
        startActivity(intent);
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }
}
