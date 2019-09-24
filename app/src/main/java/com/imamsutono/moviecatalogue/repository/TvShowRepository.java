package com.imamsutono.moviecatalogue.repository;

import androidx.lifecycle.MutableLiveData;

import com.imamsutono.moviecatalogue.model.TvShowResponse;
import com.imamsutono.moviecatalogue.service.ServiceGenerator;
import com.imamsutono.moviecatalogue.service.ServiceInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvShowRepository {
    private static TvShowRepository tvShowRepository;

    public static TvShowRepository getInstance() {
        if (tvShowRepository == null) {
            tvShowRepository = new TvShowRepository();
        }

        return tvShowRepository;
    }

    private ServiceInterface service;

    private TvShowRepository() {
        service = ServiceGenerator.createService(ServiceInterface.class);
    }

    public MutableLiveData<TvShowResponse> getTvShows() {
        final MutableLiveData<TvShowResponse> tvShowData = new MutableLiveData<>();

        service.getTvShow().enqueue(new Callback<TvShowResponse>() {
            @Override
            public void onResponse(Call<TvShowResponse> call, Response<TvShowResponse> response) {
                if (response.isSuccessful()) {
                    tvShowData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<TvShowResponse> call, Throwable t) {
                tvShowData.setValue(null);
            }
        });

        return tvShowData;
    }
}
