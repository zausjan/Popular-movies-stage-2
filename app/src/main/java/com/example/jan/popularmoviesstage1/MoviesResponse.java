package com.example.jan.popularmoviesstage1;

/**
 * Created by janko on 5/11/18.
 */
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;

import java.util.List;

public class MoviesResponse {

    @SerializedName("results")
    @Expose
    private List<Movie> movies = null;


    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}
