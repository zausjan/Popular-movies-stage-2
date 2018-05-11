package com.example.jan.popularmoviesstage1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jan.popularmoviesstage1.data.MoviesContract;
import com.example.jan.popularmoviesstage1.data.MoviesDBHelper;
import com.squareup.picasso.Picasso;


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

        assert intent != null;
        movie = getIntent().getParcelableExtra(EXTRA_MOVIE);

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
        Picasso.with(this).load(MovieListFragment.SimpleRecyclerViewAdapter.IMAGE_BASE_URL +
                posterUrl).into(posterIv);

        TextView releaseYearTv = findViewById(R.id.tv_release_year);
        String releaseDate = movie.getmReleaseYear();
        String releaseYear = releaseDate.split("-")[0];
        releaseYearTv.setText(releaseYear);

        TextView ratingTv = findViewById(R.id.tv_movie_rating);
        ratingTv.setText(movie.getmRating());

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


    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private boolean isFavorite(Movie movie) {
        SQLiteOpenHelper dbHelper = new MoviesDBHelper(this);
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String query = "SELECT * FROM  movies WHERE _id=?";
            Cursor cursor = db.rawQuery(query, new String[]{movie.getmId()});
            return cursor.getCount() > 0;

        } catch(SQLiteException e) {
            Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void onFavoriteClicked() {
        Button favorite = findViewById(R.id.favorite);

        if(isFavorite(movie)){
            //delete db entry
            SQLiteOpenHelper dbHelper = new MoviesDBHelper(this);
            try {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                int deleted = db.delete(MoviesContract.MovieEntry.TABLE_MOVIES,
                        MoviesContract.MovieEntry._ID + " = ?" ,
                        new String[]{movie.getmId()}
                        );
            } catch(SQLiteException e) {
                Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT).show();
            }
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
