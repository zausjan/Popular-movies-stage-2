package com.example.jan.popularmoviesstage1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivityFragment extends Fragment implements AsyncTaskCompleteListener<ArrayList<Movie>> {

    private MovieAdapter movieAdapter;
    private ArrayList<Movie> movieList;

    private String sortBy;

    private GridView gridView;

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
    public void onSaveInstanceState(Bundle outState){
        outState.putParcelableArrayList(sortBy, movieList);
        outState.putString("SORT_BY", sortBy);

        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        if(sortBy.equals(getString(R.string.key_popular))){
            getActivity().setTitle(R.string.title_popular);
        }
        else {
            getActivity().setTitle(R.string.title_top_rated);
        }

        View rootView = inflater.inflate(R.layout.fragment_main_activity, container, false);
        gridView = rootView.findViewById(R.id.movies_gv);

        if(movieList == null){
            return rootView;
        }

        movieAdapter = new MovieAdapter(getActivity(), movieList);
        gridView.setAdapter(movieAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Movie movie = movieList.get(position);
                launchDetailActivity(position, movie);
            }
        });

        return rootView;
    }

    private void launchDetailActivity(int position, Movie movie) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_POSITION, position);
        intent.putExtra(DetailActivity.EXTRA_MOVIE, movie);
        startActivity(intent);

    }


    @Override
    public void onTaskComplete(ArrayList<Movie> result) {
        if(result == null){
            Toast.makeText(getActivity(), R.string.error_network, Toast.LENGTH_LONG).show();
            return;
        }
        movieList = result;
        movieAdapter = new MovieAdapter(getActivity(), movieList);
        gridView.setAdapter(movieAdapter);
    }
}
