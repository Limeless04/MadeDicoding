package com.imamsutono.moviecatalogue.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class TvShow implements Parcelable {
    @SerializedName("id")
    private int id;

    @SerializedName("backdrop_path")
    private String poster;

    @SerializedName("name")
    private String title;

    @SerializedName("first_air_date")
    private String year;

    @SerializedName("vote_count")
    private String voters;

    @SerializedName("original_language")
    private String language;

    @SerializedName("vote_average")
    private String score;

    @SerializedName("overview")
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPoster() {
        return "https://image.tmdb.org/t/p/original" + poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getVoters() {
        return voters;
    }

    public void setVoters(String voters) {
        this.voters = voters;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(this.poster);
        parcel.writeString(this.title);
        parcel.writeString(this.year);
        parcel.writeString(this.voters);
        parcel.writeString(this.score);
        parcel.writeString(this.description);
    }

    public TvShow() {}

    private TvShow(Parcel in) {
        this.poster = in.readString();
        this.title = in.readString();
        this.year = in.readString();
        this.voters = in.readString();
        this.score = in.readString();
        this.description = in.readString();
    }

    public static final Creator<TvShow> CREATOR = new Creator<TvShow>() {
        @Override
        public TvShow createFromParcel(Parcel in) {
            return new TvShow(in);
        }

        @Override
        public TvShow[] newArray(int size) {
            return new TvShow[size];
        }
    };
}
