package com.example.jan.popularmoviesstage1.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by janko on 5/16/18.
 */

public class TrailersResponse {
    @SerializedName("results")
    @Expose
    private List<Trailer> trailers = null;

    public List<Trailer> getTrailers() {
        return trailers;
    }

    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
    }
}
