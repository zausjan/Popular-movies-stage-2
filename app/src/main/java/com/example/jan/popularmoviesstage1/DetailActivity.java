package com.example.jan.popularmoviesstage1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jan.popularmoviesstage1.data.MoviesContract;
import com.example.jan.popularmoviesstage1.model.Movie;
import com.squareup.picasso.Picasso;

import static com.example.jan.popularmoviesstage1.MovieListFragment.SimpleRecyclerViewAdapter.IMAGE_BASE_URL;


public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    public static final String EXTRA_MOVIE = "extra_movie";

    private Movie movie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }
        movie = getIntent().getParcelableExtra(EXTRA_MOVIE);

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            closeOnError();
            return;
        }
        if(savedInstanceState == null) {
            updateFragments();
        }
        populateUI(movie);
    }

    private void populateUI(Movie movie){
        TextView titleTv = findViewById(R.id.tv_movie_title);
        titleTv.setText(movie.getmTitle());

        ImageView posterIv = findViewById(R.id.iv_movie_poster);
        String posterUrl = movie.getmPoster();
        Picasso.with(this)
                .load(IMAGE_BASE_URL + posterUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(posterIv);

        TextView releaseYearTv = findViewById(R.id.tv_release_year);
        String releaseDate = movie.getmReleaseYear();
        String releaseYear = releaseDate.split("-")[0];
        releaseYearTv.setText(releaseYear);

        TextView ratingTv = findViewById(R.id.tv_movie_rating);
        ratingTv.setText(movie.getmRating()+ "/10");

        TextView overviewTv = findViewById(R.id.tv_movie_description);
        overviewTv.setText(movie.getmOverview());

        Button favorite = findViewById(R.id.favorite);
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFavoriteClicked();
            }
        });
        if(isFavorite(movie)){
            favorite.setBackground(getResources().getDrawable(R.mipmap.favorite_checked));
        }
        else {
            favorite.setBackground(getResources().getDrawable(R.mipmap.favorite_unchecked));
        }

    }

    private void updateFragments(){
        Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_MOVIE, movie);

        TrailersFragment trailersFragment = new TrailersFragment();
        trailersFragment.setArguments(intent.getExtras());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_trailers, trailersFragment);
        transaction.commit();

        ReviewsFragment reviewsFragment = new ReviewsFragment();
        reviewsFragment.setArguments(intent.getExtras());
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_reviews, reviewsFragment);
        transaction.commit();
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private boolean isFavorite(Movie movie) {
        Cursor cursor = this.getContentResolver()
                .query(MoviesContract.MovieEntry.CONTENT_URI,
                        null,
                        MoviesContract.MovieEntry._ID + " = ?" ,
                        new String[]{movie.getmId()},
                        null,
                        null
                );
        return cursor.getCount() > 0;
    }

    public void onFavoriteClicked() {
        Button favorite = findViewById(R.id.favorite);

        if(isFavorite(movie)){
            //delete db entry
            this.getContentResolver()
                    .delete(MoviesContract.MovieEntry.CONTENT_URI,
                    MoviesContract.MovieEntry._ID + " = ?" ,
                    new String[]{movie.getmId()}
            );
            favorite.setBackground(getResources().getDrawable(R.mipmap.favorite_unchecked));
        }
        else{
            //add db entry
            ContentValues item = new ContentValues();
            item.put(MoviesContract.MovieEntry._ID, Integer.valueOf(movie.getmId()));
            item.put(MoviesContract.MovieEntry.COLUMN_TITLE, movie.getmTitle());
            item.put(MoviesContract.MovieEntry.COLUMN_POSTER, movie.getmPoster());
            item.put(MoviesContract.MovieEntry.COLUMN_OVERVIEW, movie.getmOverview());
            item.put(MoviesContract.MovieEntry.COLUMN_RATING, movie.getmRating());
            item.put(MoviesContract.MovieEntry.COLUMN_RELEASE, movie.getmReleaseYear());

            this.getContentResolver().insert(MoviesContract.MovieEntry.CONTENT_URI, item);

            favorite.setBackground(getResources().getDrawable(R.mipmap.favorite_checked));
        }

    }
}
