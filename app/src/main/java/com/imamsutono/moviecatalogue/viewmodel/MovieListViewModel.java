package com.imamsutono.moviecatalogue.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.imamsutono.moviecatalogue.model.MovieResponse;
import com.imamsutono.moviecatalogue.repository.MovieRepository;

public class MovieListViewModel extends ViewModel {
    private MutableLiveData<MovieResponse> movieData;
    private MovieRepository movieRepository = MovieRepository.getInstance();

    public void init() {
        if (movieData == null) {
            movieData = movieRepository.getMovies();
        }
    }

    public void search(String query) {
        movieData = movieRepository.searchMovie(query);
    }

    public LiveData<MovieResponse> getMovies() {
        return movieData;
    }
}
