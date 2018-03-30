package com.example.jan.popularmoviesstage1;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by janko on 3/17/18.
 */

public class MovieFetcher extends AsyncTask<String, Void, String>{
    private static final String API_KEY = "INSERT KEY HERE";


    @Override
    protected String doInBackground(String... sortBy) {
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


            for(String line; (line = in.readLine()) != null; response += line);
        } catch (Exception e1) {
            e1.printStackTrace();
            return null;
        }
        return response;
    }

}
