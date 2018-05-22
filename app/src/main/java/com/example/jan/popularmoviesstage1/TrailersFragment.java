package com.example.jan.popularmoviesstage1;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jan.popularmoviesstage1.model.Movie;
import com.example.jan.popularmoviesstage1.model.Trailer;
import com.example.jan.popularmoviesstage1.model.TrailersResponse;

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
public class TrailersFragment extends Fragment {

    public static final String EXTRA_MOVIE = "extra_movie";

    private RecyclerView trailersRv;
    private Movie movie;


    public TrailersFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = this.getArguments();
        if(b != null){
            movie = b.getParcelable(EXTRA_MOVIE);
        }
        if(savedInstanceState == null){
            fetchTrailers();
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_trailers, container, false);
        trailersRv = rootView.findViewById(R.id.trailers_rv);
        if(trailersRv.getParent()!=null)
            ((ViewGroup)trailersRv.getParent()).removeView(trailersRv);
        setupTrailersList();
        return trailersRv;
    }

    private void fetchTrailers(){
        Log.d("FEETCH", "fetchTrailers: ");
        final String BASE_URL = "http://api.themoviedb.org/3/movie/";

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        ThemoviedbAPI api = retrofit.create(ThemoviedbAPI.class);
        Call<TrailersResponse> call = api.getTrailers(movie.getmId());
        call.enqueue(new Callback<TrailersResponse>(){

            @Override
            public void onResponse(Call<TrailersResponse> call, Response<TrailersResponse> response) {
                ArrayList<Trailer> result = (ArrayList<Trailer>) response.body().getTrailers();
                if(result == null){
                    Toast.makeText(getActivity(), R.string.error_network, Toast.LENGTH_LONG).show();
                    return;
                }
                movie.setmTrailers(result);
                setupTrailersList();
            }

            @Override
            public void onFailure(Call<TrailersResponse> call, Throwable t) {
                Toast.makeText(getActivity(), R.string.error_network, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupTrailersList() {
        trailersRv.setLayoutManager(new LinearLayoutManager(trailersRv.getContext()));
        if (movie.getmTrailers() == null) {
            return;
        }
        trailersRv.setAdapter(new SimpleRecyclerViewAdapter(getContext(),
                movie.getmTrailers()));
        trailersRv.setNestedScrollingEnabled(false);
    }


    public static class SimpleRecyclerViewAdapter extends
            RecyclerView.Adapter<TrailersFragment.SimpleRecyclerViewAdapter.ViewHolder>{

        private List<Trailer> mTrailers;
        private Context mContext;

        SimpleRecyclerViewAdapter(Context context, List<Trailer> trailers) {
            mTrailers = trailers;
        }

        static class ViewHolder extends RecyclerView.ViewHolder{
            Trailer mBoundTrailer;
            final View mView;
            final TextView mTrailerNameTv;
            int mPosition;
            Context mContext;

            ViewHolder(View view) {
                super(view);
                mView = view;
                mTrailerNameTv = view.findViewById(R.id.trailer_name);
                mContext = view.getContext();
            }
        }

        @Override
        public TrailersFragment.SimpleRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.trailer_list_item, parent, false);
            return new TrailersFragment.SimpleRecyclerViewAdapter.ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull final TrailersFragment.SimpleRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.mPosition = holder.getAdapterPosition();
            holder.mBoundTrailer = getItem(holder.mPosition);
            holder.mTrailerNameTv.setText(holder.mBoundTrailer.getName());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    Context context = v.getContext();
                    Intent appIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("vnd.youtube:" + holder.mBoundTrailer.getKey()));
                    Intent webIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.youtube.com/watch?v=" + holder.mBoundTrailer.getKey()));
                    try {
                        context.startActivity(appIntent);
                    } catch (ActivityNotFoundException ex) {
                        context.startActivity(webIntent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mTrailers.size();
        }

        Trailer getItem(int position){
            return mTrailers.get(position);
        }

    }
}

