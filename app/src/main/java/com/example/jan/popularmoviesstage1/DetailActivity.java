package com.example.jan.popularmoviesstage1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    public static final String EXTRA_MOVIE = "extra_movie";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }


        assert intent != null;
        Movie movie = getIntent().getParcelableExtra(EXTRA_MOVIE);


        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            closeOnError();
            return;
        }

        populateUI(movie);
    }

    private void populateUI(Movie movie){
        TextView titleTv = findViewById(R.id.tv_movie_title);
        titleTv.setText(movie.getmTitle());

        ImageView posterIv = findViewById(R.id.iv_movie_poster);
        String posterUrl = movie.getmPoster();
        Picasso.with(this).load(MovieListFragment.SimpleRecyclerViewAdapter.IMAGE_BASE_URL + posterUrl).into(posterIv);

        TextView releaseYearTv = findViewById(R.id.tv_release_year);
        String releaseDate = movie.getmReleaseYear();
        String releaseYear = releaseDate.split("-")[0];
        releaseYearTv.setText(releaseYear);

        TextView ratingTv = findViewById(R.id.tv_movie_rating);
        ratingTv.setText(movie.getmRating());

        TextView overviewTv = findViewById(R.id.tv_movie_description);
        overviewTv.setText(movie.getmOverview());

    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }
}
