package com.imamsutono.moviecatalogue.ui.movie;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.imamsutono.moviecatalogue.R;

public class MovieDetailFragment extends Fragment {

    public static final String EXTRA_ID = "extra_id";

    public MovieDetailFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;
    }

}
