package com.imamsutono.moviecatalogue.ui.tvshow;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.imamsutono.moviecatalogue.R;

public class TvShowDetailFragment extends Fragment {

    public static final String EXTRA_ID = "extra_id";

    public TvShowDetailFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.app_name);
        return textView;
    }

}
