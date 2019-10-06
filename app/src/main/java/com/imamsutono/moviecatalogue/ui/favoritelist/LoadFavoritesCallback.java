package com.imamsutono.moviecatalogue.ui.favoritelist;

import com.imamsutono.moviecatalogue.model.Movie;
import com.imamsutono.moviecatalogue.model.TvShow;

import java.util.List;

public interface LoadFavoritesCallback {
    void preExecute();
    void postExecuteMovie(List<Movie> movies);
    void postExecuteTvShow(List<TvShow> tvShows);
}
