package com.example.jan.popularmoviesstage1;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by janko on 3/25/18.
 */

public class MovieAdapter extends ArrayAdapter<Movie> {

    public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185";
    private List<Movie> mMovies;

    public MovieAdapter(Activity context, List<Movie> movies) {
        super(context, 0, movies);
        mMovies = movies;
    }

    public List<Movie> getItems(){
        return mMovies;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Movie movie = getItem(position);
        View gridView;
        ImageView imageView;

        if (convertView == null && inflater != null) {
            gridView = inflater.inflate(R.layout.movie_grid_item, null);
            imageView = gridView.findViewById(R.id.movie_grid_iv);
        } else {
           gridView = convertView;
           imageView = gridView.findViewById(R.id.movie_grid_iv);

        }

        String poster_url = movie != null ? movie.getmPoster() : null;
        Picasso.with(getContext()).load(IMAGE_BASE_URL + poster_url).into(imageView);

        return gridView;
    }

}
