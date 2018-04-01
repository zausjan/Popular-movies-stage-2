package com.example.jan.popularmoviesstage1;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * Created by janko on 3/17/18.
 */

public class MovieFetcher extends AsyncTask<String, Void, String>{

    private static final String API_KEY = BuildConfig.API_KEY;
    private Context context;
    private AsyncTaskCompleteListener<ArrayList<Movie>> listener;

    public MovieFetcher(Context context, AsyncTaskCompleteListener<ArrayList<Movie>> listener){
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... sortBy) {
        if(!isOnline()){
            Log.e("Network error", "Error obtaining network connection");
            return null;
        }

        String response = "";
        final String base_url = "http://api.themoviedb.org/3/movie/";
        String url_string = base_url + sortBy[0] + "?api_key=" + API_KEY;
        HttpURLConnection connection;
        try {
            URL url = new URL(url_string);
            connection = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            connection.getInputStream()
                    ));

            // get String from response
            for(String line; (line = in.readLine()) != null; response += line);

        } catch (Exception e1) {
            e1.printStackTrace();
            return null;
        }
        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        ArrayList<Movie> movieList = null;
        if(response != null){
            movieList = getMovieData(response);
        }

        listener.onTaskComplete(movieList);
    }

    private ArrayList<Movie> getMovieData(String jsonString){

        JSONObject jsonObject;
        JSONArray data;

        ArrayList<Movie> moviesList = new ArrayList<>();
        try {
            jsonObject = new JSONObject(jsonString);
            data = jsonObject.getJSONArray("results");

            for(int i = 0; i < data.length(); i++){
                moviesList.add(JsonUtils.parseMovie(data.getJSONObject(i).toString()));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return moviesList;
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm != null ? cm.getActiveNetworkInfo() : null;
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
