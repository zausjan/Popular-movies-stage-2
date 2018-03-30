package com.example.jan.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class MainActivityFragment extends Fragment {

    private MovieAdapter movieAdapter;
    private ArrayList<Movie> movieList;

    private String sortBy;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = this.getArguments();
        if(b != null){
            sortBy = b.getString("SORT_BY");
        }

        if(sortBy == null){
            sortBy = getString(R.string.key_popular);
        }

        if(savedInstanceState == null || !savedInstanceState.containsKey(sortBy)){
            movieList = getMovieData(sortBy);
        }
        else{
            movieList = savedInstanceState.getParcelableArrayList(sortBy);
        }

    }

    public MainActivityFragment(){}

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putParcelableArrayList(sortBy, movieList);
        outState.putString("SORT_BY", sortBy);

        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_main_activity, container, false);

        if(movieList == null){
            Toast.makeText(getActivity(), R.string.error_network, Toast.LENGTH_LONG).show();
            return rootView;
        }
        movieAdapter = new MovieAdapter(getActivity(), movieList);

        GridView gridview = rootView.findViewById(R.id.movies_gv);
        gridview.setAdapter(movieAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Movie movie = movieList.get(position);
                launchDetailActivity(position, movie);
            }
        });

        return rootView;
    }

    private void launchDetailActivity(int position, Movie movie) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_POSITION, position);
        intent.putExtra(DetailActivity.EXTRA_MOVIE, movie);
        startActivity(intent);

    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm != null ? cm.getActiveNetworkInfo() : null;
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private ArrayList<Movie> getMovieData(String sortBy){
        String response = "";

        if(!isOnline()){
            Log.e("Network error", "Error obtaining network connection");
            return null;
        }

        try {
            response = new MovieFetcher().execute(sortBy).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject;
        JSONArray data;

        ArrayList<Movie> moviesList = new ArrayList<>();
        try {
            jsonObject = new JSONObject(response);
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

}
