package com.example.jan.popularmoviesstage1;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    public String sortBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_popular);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            updateFragment(sortBy);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movies_menu, menu);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("SORT_BY", sortBy);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.sortby_popular:
                sortBy = getString(R.string.key_popular);
                setTitle(R.string.title_popular);
                break;
            case R.id.sortby_top_rated:
                sortBy = getString(R.string.key_top_rated);
                setTitle(R.string.title_top_rated);
                break;
            case R.id.sortby_favorites:
                sortBy = getString(R.string.key_favorites);
                setTitle(R.string.title_favorites);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        updateFragment(sortBy);

        return true;
    }

    private void updateFragment(String sortBy){
        Bundle args = new Bundle();
        args.putString("SORT_BY", sortBy);

        MovieListFragment fragment = new MovieListFragment();
        fragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_main, fragment);
        transaction.commit();
    }
}
