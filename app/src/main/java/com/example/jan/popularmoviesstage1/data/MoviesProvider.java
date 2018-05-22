package com.example.jan.popularmoviesstage1.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by janko on 5/1/18.
 */

public class MoviesProvider extends ContentProvider{
    private static final String LOG_TAG = MoviesProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MoviesDBHelper mOpenHelper;

    private static final int MOVIE = 100;
    private static final int MOVIE_WITH_ID = 200;


    private static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MoviesContract.MovieEntry.TABLE_MOVIES, MOVIE);
        matcher.addURI(authority, MoviesContract.MovieEntry.TABLE_MOVIES+ "/#", MOVIE_WITH_ID);
        return matcher;
    }
    @Override
    public boolean onCreate() {
        mOpenHelper = new MoviesDBHelper((getContext()));
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.MovieEntry.TABLE_MOVIES,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                return retCursor;
            case MOVIE_WITH_ID:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.MovieEntry.TABLE_MOVIES,
                        projection,
                        MoviesContract.MovieEntry._ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder
                );
                return retCursor;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case MOVIE:
                return  MoviesContract.MovieEntry.CONTENT_DIR_TYPE;
            case MOVIE_WITH_ID:
                return MoviesContract.MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri;
        switch (sUriMatcher.match(uri)){
            case MOVIE:
                long _id = db.insert(MoviesContract.MovieEntry.TABLE_MOVIES, null, values);
                if(_id > 0) {
                    returnUri = MoviesContract.MovieEntry.builMoviesUri(_id);
                }
                else{
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }
    @Override
    public int bulkInsert(Uri uri, ContentValues[] values){
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch(match){
            case MOVIE:
                db.beginTransaction();
                int numInserted = 0;
                try{
                    for(ContentValues value : values){
                        if (value == null){
                            throw new IllegalArgumentException("Cannot have null content values");
                        }
                        long _id = -1;
                        try{
                            _id = db.insertOrThrow(MoviesContract.MovieEntry.TABLE_MOVIES,
                                    null, value);
                        }catch(SQLiteConstraintException e) {
                            e.printStackTrace();
                            Log.w(LOG_TAG, "Attempting to insert " +
                                    value.getAsString(
                                            MoviesContract.MovieEntry.COLUMN_TITLE)
                                    + " but value is already in database.");
                        }
                        if (_id != -1){
                            numInserted++;
                        }
                    }
                    if(numInserted > 0){
                        db.setTransactionSuccessful();
                    }
                } finally {
                    db.endTransaction();
                }
                if (numInserted > 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return numInserted;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int numDeleted;
        switch(match){
            case MOVIE:
                numDeleted = db.delete(
                        MoviesContract.MovieEntry.TABLE_MOVIES, selection, selectionArgs);
                break;
            case MOVIE_WITH_ID:
                numDeleted = db.delete(MoviesContract.MovieEntry.TABLE_MOVIES,
                        MoviesContract.MovieEntry._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return numDeleted;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int numUpdated = 0;

        if (values == null){
            throw new IllegalArgumentException("Cannot have null content values");
        }

        switch(sUriMatcher.match(uri)){
            case MOVIE:{
                numUpdated = db.update(MoviesContract.MovieEntry.TABLE_MOVIES,
                        values,
                        selection,
                        selectionArgs);
                break;
            }
            case MOVIE_WITH_ID: {
                numUpdated = db.update(MoviesContract.MovieEntry.TABLE_MOVIES,
                        values,
                        MoviesContract.MovieEntry._ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))});
                break;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        if (numUpdated > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numUpdated;
    }
}
