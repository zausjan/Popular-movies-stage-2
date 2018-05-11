package com.example.jan.popularmoviesstage1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by janko on 5/1/18.
 */

public class MoviesDBHelper extends SQLiteOpenHelper{
    public static final String LOG_TAG = MoviesDBHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;

    public MoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
                MoviesContract.MovieEntry.TABLE_MOVIES + "(" + MoviesContract.MovieEntry._ID+
                " INTEGER PRIMARY KEY," +
                MoviesContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MoviesContract.MovieEntry.COLUMN_RELEASE + " TEXT , " +
                MoviesContract.MovieEntry.COLUMN_RATING + " TEXT , " +
                MoviesContract.MovieEntry.COLUMN_POSTER + " TEXT , " +
                MoviesContract.MovieEntry.COLUMN_OVERVIEW + " TEXT );";
        db.execSQL(SQL_CREATE_MOVIE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to " +
                newVersion + ". OLD DATA WILL BE DESTROYED");

        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MovieEntry.TABLE_MOVIES);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
        MoviesContract.MovieEntry.TABLE_MOVIES + "'");
        onCreate(db);
    }
}
