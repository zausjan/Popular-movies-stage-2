package com.example.jan.popularmoviesstage1;

import android.app.Activity;
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
        Movie movie = getItem(position);
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(getContext());
            // I don't know how to not hardcode Height
            imageView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, 780));
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        String poster_url = movie != null ? movie.getmPoster() : null;
        Picasso.with(getContext()).load(IMAGE_BASE_URL + poster_url).into(imageView);

        return imageView;
    }
}
