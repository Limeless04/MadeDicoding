package com.imamsutono.moviecatalogue.repository;

import androidx.lifecycle.MutableLiveData;

import com.imamsutono.moviecatalogue.model.MovieResponse;
import com.imamsutono.moviecatalogue.service.ServiceGenerator;
import com.imamsutono.moviecatalogue.service.ServiceInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {
    private static MovieRepository movieRepository;

    public static MovieRepository getInstance() {
        if (movieRepository == null) {
            movieRepository = new MovieRepository();
        }

        return movieRepository;
    }

    private ServiceInterface service;

    private MovieRepository() {
        service = ServiceGenerator.createService(ServiceInterface.class);
    }

    public MutableLiveData<MovieResponse> getMovies() {
        final MutableLiveData<MovieResponse> movieData = new MutableLiveData<>();

        service.getMovie().enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    movieData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                movieData.setValue(null);
            }
        });

        return movieData;
    }
}
