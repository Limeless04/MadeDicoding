package com.imamsutono.moviecatalogue.ui.tvshow;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.imamsutono.moviecatalogue.model.TvShowResponse;
import com.imamsutono.moviecatalogue.repository.TvShowRepository;

public class TvShowListViewModel extends ViewModel {
    private MutableLiveData<TvShowResponse> tvShowData;

    public void init() {
        if (tvShowData == null) {
            TvShowRepository tvShowRepository;
            tvShowRepository = TvShowRepository.getInstance();
            tvShowData = tvShowRepository.getTvShows();
        }
    }

    public LiveData<TvShowResponse> getTvShows() {
        return tvShowData;
    }
}
