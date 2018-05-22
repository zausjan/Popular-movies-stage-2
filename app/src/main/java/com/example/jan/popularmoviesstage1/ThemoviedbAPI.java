package com.example.jan.popularmoviesstage1;

import com.example.jan.popularmoviesstage1.model.MoviesResponse;
import com.example.jan.popularmoviesstage1.model.ReviewsResponse;
import com.example.jan.popularmoviesstage1.model.TrailersResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by janko on 5/1/18.
 */

public interface ThemoviedbAPI {
    String API_KEY = BuildConfig.API_KEY;
    String request_url =  "?api_key=" + API_KEY;

    @GET("{SORT_BY}" + request_url)
    Call<MoviesResponse> getMovies(@Path("SORT_BY") String sortBy);

    @GET("{ID}/videos" + request_url )
    Call<TrailersResponse> getTrailers(@Path("ID") String id);

    @GET("{ID}/reviews" + request_url )
    Call<ReviewsResponse> getReviews(@Path("ID") String id);
}
