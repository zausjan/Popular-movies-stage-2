package com.example.jan.popularmoviesstage1;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by janko on 5/1/18.
 */

public interface ThemoviedbAPI {
    String BASE_URL = "http://api.themoviedb.org/3/movie/";
    String API_KEY = BuildConfig.API_KEY;

    String request_url =  "?api_key=" + API_KEY;

    @GET("{SORT_BY}" + request_url)
    Call<MoviesResponse> getMovies(@Path("SORT_BY") String sortBy);
}
