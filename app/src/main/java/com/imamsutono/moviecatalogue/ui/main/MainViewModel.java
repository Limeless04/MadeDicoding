package com.imamsutono.moviecatalogue.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.imamsutono.moviecatalogue.model.MovieResponse;
import com.imamsutono.moviecatalogue.model.TvShowResponse;
import com.imamsutono.moviecatalogue.repository.MovieRepository;
import com.imamsutono.moviecatalogue.repository.TvShowRepository;

public class MainViewModel extends ViewModel {
    private MutableLiveData<MovieResponse> movieData;
    private MutableLiveData<MovieResponse> todayReleaseMovie;
    private MutableLiveData<TvShowResponse> tvShowData;
    private MovieRepository movieRepository = MovieRepository.getInstance();

    public void init(String date) {
        if (movieData == null) {
            movieData = movieRepository.getMovies();
        }

        if (tvShowData == null) {
            TvShowRepository tvShowRepository = TvShowRepository.getInstance();
            tvShowData = tvShowRepository.getTvShows();
        }

        if (todayReleaseMovie == null) {
            todayReleaseMovie = movieRepository.getTodayRelease(date);
        }
    }

    public LiveData<MovieResponse> getMovies() {
        return movieData;
    }

    LiveData<MovieResponse> getTodayReleaseMovie() {
        return todayReleaseMovie;
    }

    public LiveData<TvShowResponse> getTvShows() {
        return tvShowData;
    }
}
