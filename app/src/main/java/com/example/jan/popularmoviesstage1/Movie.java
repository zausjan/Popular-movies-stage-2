package com.example.jan.popularmoviesstage1;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by janko on 3/17/18.
 */


public class Movie implements Parcelable {
    @SerializedName("id")
    @Expose
    private String mId;

    @SerializedName("title")
    @Expose
    private String mTitle;

    @SerializedName("poster_path")
    @Expose
    private String mPoster;

    @SerializedName("overview")
    @Expose
    private String mOverview;

    @SerializedName("vote_average")
    @Expose
    private String mRating;

    @SerializedName("release_date")
    @Expose
    private String mReleaseYear;


    private ArrayList<Trailer> mTrailers;

    public Movie(String mId, String mTitle, String mPoster, String mOverview,
                 String mRating, String mReleaseYear) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mPoster = mPoster;
        this.mOverview = mOverview;
        this.mRating = mRating;
        this.mReleaseYear = mReleaseYear;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmPoster() {
        return mPoster;
    }

    public void setmPoster(String mPoster) {
        this.mPoster = mPoster;
    }
    public String getmOverview() {
        return mOverview;
    }

    public void setmOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    public String getmRating() {
        return mRating;
    }

    public void setmRating(String mRating) {
        this.mRating = mRating;
    }

    public String getmReleaseYear() {
        return mReleaseYear;
    }

    public void setmReleaseYear(String mReleaseYear) {
        this.mReleaseYear = mReleaseYear;
    }

    public ArrayList<Trailer> getmTrailers() {
        return mTrailers;
    }

    public void setmTrailers(ArrayList<Trailer> mTrailers) {
        this.mTrailers = mTrailers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeString(this.mTitle);
        dest.writeString(this.mPoster);
        dest.writeString(this.mOverview);
        dest.writeString(this.mRating);
        dest.writeString(this.mReleaseYear);
    }

    protected Movie(Parcel in) {
        this.mId = in.readString();
        this.mTitle = in.readString();
        this.mPoster = in.readString();
        this.mOverview = in.readString();
        this.mRating = in.readString();
        this.mReleaseYear = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}