package com.example.jan.popularmoviesstage1;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by janko on 3/17/18.
 */

public class JsonUtils {

    public static Movie parseMovie(String json){
        Movie movie = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            String id = jsonObject.optString("id");
            String title = jsonObject.optString("title");
            String poster = jsonObject.optString("poster_path");
            String overview = jsonObject.optString("overview");
            String rating = jsonObject.optString("vote_average");
            String releaseYear = jsonObject.optString("release_date");

            movie = new Movie(id, title, poster, overview, rating, releaseYear);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    return movie;
    }
}
