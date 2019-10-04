package com.imamsutono.moviecatalogue.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.imamsutono.moviecatalogue.model.MovieResponse;
import com.imamsutono.moviecatalogue.repository.MovieRepository;

public class MovieListViewModel extends ViewModel {
    private MutableLiveData<MovieResponse> movieData;

    public void init() {
        if (movieData == null) {
            MovieRepository movieRepository;
            movieRepository = MovieRepository.getInstance();
            movieData = movieRepository.getMovies();
        }
    }

    public LiveData<MovieResponse> getMovies() {
        return movieData;
    }
}
