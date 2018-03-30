package com.example.jan.popularmoviesstage1;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by janko on 3/25/18.
 */

public class MovieAdapter extends ArrayAdapter<Movie> {

    public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185";


    public MovieAdapter(Activity context, List<Movie> movies) {
        super(context, 0, movies);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Movie movie = getItem(position);
        View gridView;
        ImageView imageView;

        if (convertView == null) {
            gridView = inflater.inflate(R.layout.movie_grid_item, null);
            // I don't know how to not hardcode Height
            imageView = gridView.findViewById(R.id.movie_grid_iv);
           gridView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, 780));
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
           gridView = (View) convertView;
           imageView = gridView.findViewById(R.id.movie_grid_iv);

        }

        String poster_url = movie != null ? movie.getmPoster() : null;
        Picasso.with(getContext()).load(IMAGE_BASE_URL + poster_url).into(imageView);

        return gridView;
    }
}
