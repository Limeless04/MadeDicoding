package com.imamsutono.moviecatalogue.service;

import com.imamsutono.moviecatalogue.model.Movie;
import com.imamsutono.moviecatalogue.model.MovieResponse;
import com.imamsutono.moviecatalogue.model.TvShow;
import com.imamsutono.moviecatalogue.model.TvShowResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.imamsutono.moviecatalogue.service.ServiceGenerator.API_KEY;

public interface ServiceInterface {
    @GET("/3/discover/movie?api_key=" + API_KEY + "&language=en-US")
    Call<MovieResponse> getMovie();

    @GET("/3/movie/{id}?api_key=" + API_KEY + "&language=en-US")
    Call<Movie> getMovieDetail(@Path("id") int id);

    @GET("/3/search/movie?api_key=" + API_KEY + "&language=en-US&page=1&include_adult=false")
    Call<MovieResponse> searchMovie(@Query("query") String query);

    @GET("/3/discover/tv?api_key=" + API_KEY + "&language=en-US")
    Call<TvShowResponse> getTvShow();

    @GET("/3/tv/{id}?api_key=" + API_KEY + "&language=en-US")
    Call<TvShow> getTvShowDetail(@Path("id") int id);

    @GET("/3/search/tv?api_key=" + API_KEY + "&language=en-US&page=1")
    Call<TvShowResponse> searchTvShow(@Query("query") String query);
}
