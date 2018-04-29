package com.example.jan.popularmoviesstage1;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import static com.example.jan.popularmoviesstage1.MovieListFragment.SimpleRecyclerViewAdapter.*;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieListFragment extends Fragment implements AsyncTaskCompleteListener<ArrayList<Movie>>{

    private ArrayList<Movie> movieList;
    private String sortBy;

    private RecyclerView rv;


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

        if(savedInstanceState == null || !savedInstanceState.containsKey(sortBy)) {
            new MovieFetcher(getContext(), this).execute(sortBy);
        }
        else{
            movieList = savedInstanceState.getParcelableArrayList(sortBy);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(sortBy.equals(getString(R.string.key_popular))){
            getActivity().setTitle(R.string.title_popular);
        }
        else {
            getActivity().setTitle(R.string.title_top_rated);
        }
        rv = (RecyclerView) inflater.inflate(
                R.layout.fragment_movie_list, container, false);

        setupRecyclerView(rv);
        return rv;
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putParcelableArrayList(sortBy, movieList);
        outState.putString("SORT_BY", sortBy);

        super.onSaveInstanceState(outState);
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        if(movieList == null){
            return;
        }
        recyclerView.setAdapter(new SimpleRecyclerViewAdapter(getActivity(), movieList));
    }


    @Override
    public void onTaskComplete(ArrayList<Movie> result) {
        if(result == null){
            Toast.makeText(getActivity(), R.string.error_network, Toast.LENGTH_LONG).show();
            return;
        }
        movieList = result;
        rv.setAdapter(new SimpleRecyclerViewAdapter(getActivity(), movieList));
    }

    public static class SimpleRecyclerViewAdapter extends
            RecyclerView.Adapter<SimpleRecyclerViewAdapter.ViewHolder>{

        public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185";
        private List<Movie> mMovies;


        public static class ViewHolder extends RecyclerView.ViewHolder{
            public Movie mBoundMovie;

            public final View mView;
            public final ImageView mImageView;
            public int mPosition;
            public Context mContext;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = view.findViewById(R.id.poster_iv);
                mContext = view.getContext();

            }
        }

        public SimpleRecyclerViewAdapter(Context context, List<Movie> movies) {
            mMovies = movies;
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

        public Movie getItem(int position){
            return mMovies.get(position);
        }


    }

}


