package com.imamsutono.moviecatalogue.ui.favoritelist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.imamsutono.moviecatalogue.model.MovieResponse;
import com.imamsutono.moviecatalogue.model.TvShowResponse;
import com.imamsutono.moviecatalogue.repository.MovieRepository;
import com.imamsutono.moviecatalogue.repository.TvShowRepository;

public class FavoriteListViewModel extends ViewModel {
    private MutableLiveData<MovieResponse> movieData;
    private MutableLiveData<TvShowResponse> tvShowData;

    public void init() {
        if (movieData == null) {
            MovieRepository movieRepository;
            movieRepository = MovieRepository.getInstance();
            movieData = movieRepository.getMovies();
        }

        if (tvShowData == null) {
            TvShowRepository tvShowRepository;
            tvShowRepository = TvShowRepository.getInstance();
            tvShowData = tvShowRepository.getTvShows();
        }
    }

    public LiveData<MovieResponse> getMovies() {
        return movieData;
    }

    public LiveData<TvShowResponse> getTvShows() {
        return tvShowData;
    }
}
