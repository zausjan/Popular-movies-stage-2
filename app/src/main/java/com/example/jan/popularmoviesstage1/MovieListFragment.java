package com.example.jan.popularmoviesstage1;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jan.popularmoviesstage1.data.MoviesContract;
import com.example.jan.popularmoviesstage1.model.Movie;
import com.example.jan.popularmoviesstage1.model.MoviesResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String SORT_BY = "sort_by";
    private ArrayList<Movie> movieList;
    private String sortBy;

    private RecyclerView rv;
    private SimpleRecyclerViewAdapter adapter;

    private static final int CURSOR_LOADER_ID = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = this.getArguments();
        if(b != null){
            sortBy = b.getString(SORT_BY);
        }

        if(sortBy == null){
            sortBy = getString(R.string.key_popular);
        }
        if(sortBy.equals(getString(R.string.key_popular)) || sortBy.equals(getString(R.string.key_top_rated))) {
            if(savedInstanceState == null || !savedInstanceState.containsKey(sortBy)) {
                fetchMovies(sortBy);            }
            else{
                movieList = savedInstanceState.getParcelableArrayList(sortBy);
            }
        }
        else {
            getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);
        rv = rootView.findViewById(R.id.movies_rv);
        if(rv.getParent()!=null)
            ((ViewGroup)rv.getParent()).removeView(rv);
        setupRecyclerView(rv);
        return rv;
    }


    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(sortBy, movieList);
        outState.putString(SORT_BY, sortBy);
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), numberOfColumns()));
        if(movieList == null){
            return;
        }
        adapter = new SimpleRecyclerViewAdapter(getActivity(), movieList);
        rv.setAdapter(adapter);
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }

    void fetchMovies(String sortBy){
        final String BASE_URL = "http://api.themoviedb.org/3/movie/";

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        ThemoviedbAPI api = retrofit.create(ThemoviedbAPI.class);
        Call<MoviesResponse> call = api.getMovies(sortBy);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                ArrayList<Movie> result = (ArrayList<Movie>)response.body().getMovies();
                if(result == null){
                    Toast.makeText(getActivity(), R.string.error_network, Toast.LENGTH_LONG).show();
                    return;
                }
                movieList = result;
                setupRecyclerView(rv);
            }
            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Toast.makeText(getActivity(), R.string.error_network, Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args){
        return new CursorLoader(getActivity(),
                MoviesContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        movieList = new ArrayList<Movie>();
        while(!data.isAfterLast()){
            Movie movie = new Movie(
                    data.getString(data.getColumnIndex(MoviesContract.MovieEntry._ID)),
                    data.getString(data.getColumnIndex(MoviesContract.MovieEntry.COLUMN_TITLE)),
                    data.getString(data.getColumnIndex(MoviesContract.MovieEntry.COLUMN_POSTER)),
                    data.getString(data.getColumnIndex(MoviesContract.MovieEntry.COLUMN_OVERVIEW)),
                    data.getString(data.getColumnIndex(MoviesContract.MovieEntry.COLUMN_RATING)),
                    data.getString(data.getColumnIndex(MoviesContract.MovieEntry.COLUMN_RELEASE))
                    );
            movieList.add(movie);
            data.moveToNext();
        }
        setupRecyclerView(rv);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    public static class SimpleRecyclerViewAdapter extends
            RecyclerView.Adapter<SimpleRecyclerViewAdapter.ViewHolder>{

        static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185";
        private List<Movie> mMovies;

        private Context mContext;

        SimpleRecyclerViewAdapter(Context context, List<Movie> movies) {
            mMovies = movies;
        }

        static class ViewHolder extends RecyclerView.ViewHolder{
            Movie mBoundMovie;
            final View mView;
            final ImageView mImageView;
            int mPosition;
            Context mContext;

            ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = view.findViewById(R.id.poster_iv);
                mContext = view.getContext();
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            holder.mPosition = holder.getAdapterPosition();
            holder.mBoundMovie = getItem(holder.mPosition);
            String poster_url = holder.mBoundMovie != null ? holder.mBoundMovie.getmPoster() : null;
            Picasso.with(holder.mContext).load(IMAGE_BASE_URL + poster_url).into(holder.mImageView);
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    Context context = v.getContext();
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra(DetailActivity.EXTRA_POSITION, holder.mPosition);
                    intent.putExtra(DetailActivity.EXTRA_MOVIE, holder.mBoundMovie);
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mMovies.size();
        }

        Movie getItem(int position){
            return mMovies.get(position);
        }

    }
}
