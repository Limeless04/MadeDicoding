package com.imamsutono.moviefavorite;

import com.imamsutono.moviefavorite.model.Movie;

import java.util.List;

public interface LoadFavoritesCallback {
    void preExecute();
    void postExecute(List<Movie> movies);
}
