package com.imamsutono.moviecatalogue.ui.tvshow;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.imamsutono.moviecatalogue.R;
import com.imamsutono.moviecatalogue.adapter.TvShowAdapter;
import com.imamsutono.moviecatalogue.model.TvShow;
import com.imamsutono.moviecatalogue.model.TvShowResponse;

import java.util.ArrayList;
import java.util.List;

public class TvShowListFragment extends Fragment {

    private RecyclerView rvMovies;
    private ProgressBar progressBar;
    private List<TvShow> listTvShow = new ArrayList<>();

    public TvShowListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        setHasOptionsMenu(true);

        rvMovies = view.findViewById(R.id.rv_list);
        rvMovies.setHasFixedSize(true);
        progressBar = view.findViewById(R.id.progress_list);

        TvShowListViewModel viewModel = ViewModelProviders.of(this).get(TvShowListViewModel.class);
        viewModel.init();
        viewModel.getTvShows().observe(this, getTvShow);

        showLoading();
        return view;
    }

    private Observer<TvShowResponse> getTvShow = new Observer<TvShowResponse>() {
        @Override
        public void onChanged(TvShowResponse tvShowResponse) {
            if (tvShowResponse != null) {
                List<TvShow> tvShows = tvShowResponse.getResults();
                listTvShow.addAll(tvShows);
                showTvShows();
            } else {
                Toast.makeText(getContext(), "Gagal mengambil data pertunjukan TV", Toast.LENGTH_SHORT).show();
                hideLoading();
            }
        }
    };

    private void showTvShows() {
        TvShowAdapter tvShowAdapter = new TvShowAdapter(listTvShow);
        rvMovies.setAdapter(tvShowAdapter);
        tvShowAdapter.notifyDataSetChanged();

        tvShowAdapter.setOnItemClickCallback(new TvShowAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(TvShow data) {
                openDetail(data.getId());
            }
        });
        hideLoading();
    }

    private void openDetail(int id) {
        Intent intent = new Intent(getContext(), TvShowDetailFragment.class);
        intent.putExtra(TvShowDetailFragment.EXTRA_ID, id);
        startActivity(intent);
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

}
