package com.imamsutono.moviecatalogue.ui.favorite;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.imamsutono.moviecatalogue.model.Movie;
import com.imamsutono.moviecatalogue.model.TvShow;

import java.util.List;

public class FavoriteViewModel extends ViewModel {
    private MutableLiveData<List<Movie>> movies = new MutableLiveData<>();
    private MutableLiveData<List<TvShow>> tvShows = new MutableLiveData<>();

    LiveData<List<Movie>> getMovies() {
        return movies;
    }

    LiveData<List<TvShow>> getTvShows() {
        return tvShows;
    }
}
