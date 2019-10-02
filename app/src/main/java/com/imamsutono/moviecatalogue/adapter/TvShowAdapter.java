package com.imamsutono.moviecatalogue.adapter;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.imamsutono.moviecatalogue.R;
import com.imamsutono.moviecatalogue.model.TvShow;

import java.util.ArrayList;
import java.util.List;

public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.ViewHolder> {

    private List<TvShow> tvShows = new ArrayList<>();
    private OnItemClickCallback onItemClickCallback;

    public TvShowAdapter() {}

    public TvShowAdapter(List<TvShow> tvShows) {
        this.tvShows = tvShows;
    }

    public void setData(List<TvShow> items) {
        if (items.size() > 0) {
            tvShows.clear();
        }
        tvShows.addAll(items);
        notifyDataSetChanged();
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_film, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        TvShow tvShow = tvShows.get(position);

        Glide.with(holder.itemView.getContext())
                .load(tvShow.getPoster())
                .placeholder(new ColorDrawable(Color.GRAY))
                .error(new ColorDrawable(Color.GRAY))
                .transform(new CenterCrop(), new RoundedCorners(16))
                .into(holder.imgPoster);
        holder.txtTitle.setText(tvShow.getTitle());
        holder.txtYear.setText(tvShow.getYear());
        holder.txtLanguage.setText(tvShow.getLanguage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickCallback.onItemClicked(tvShows.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return tvShows.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgPoster;
        private TextView txtTitle;
        private TextView txtYear;
        private TextView txtLanguage;

        ViewHolder(View view) {
            super(view);

            imgPoster = view.findViewById(R.id.img_poster);
            txtTitle = view.findViewById(R.id.txt_title);
            txtYear = view.findViewById(R.id.txt_year);
            txtLanguage = view.findViewById(R.id.txt_language);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(TvShow data);
    }
}
