package com.example.jan.popularmoviesstage1;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    public String sortBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        outState.putString("SORT_BY", sortBy);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sortby_popular) {
            sortBy = getString(R.string.key_popular);
        }
        else if(id == R.id.sortby_top_rated){
            sortBy = getString(R.string.key_top_rated);
        }
        else{
            return super.onOptionsItemSelected(item);
        }
        updateFragment(sortBy);

        return true;
    }

    private void updateFragment(String sortBy){
        Bundle args = new Bundle();
        args.putString("SORT_BY", sortBy);

        MainActivityFragment fragment = new MainActivityFragment();
        fragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_main, fragment);
        transaction.commit();
    }
}
