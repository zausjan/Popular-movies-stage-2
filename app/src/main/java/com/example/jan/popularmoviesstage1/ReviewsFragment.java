package com.example.jan.popularmoviesstage1;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jan.popularmoviesstage1.model.Movie;
import com.example.jan.popularmoviesstage1.model.Review;
import com.example.jan.popularmoviesstage1.model.ReviewsResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewsFragment extends Fragment {
    @BindView(R.id.reviews_rv) RecyclerView reviewsRv;
    public static final String EXTRA_MOVIE = "extra_movie";

    private Movie movie;


    public ReviewsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = this.getArguments();
        if(b != null){
            movie = b.getParcelable(EXTRA_MOVIE);
        }
        if(savedInstanceState == null){
            fetchReviews();
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reviews, container, false);
        reviewsRv = rootView.findViewById(R.id.reviews_rv);
        if(reviewsRv.getParent()!=null)
            ((ViewGroup)reviewsRv.getParent()).removeView(reviewsRv);
        setupReviewsList();
        return reviewsRv;
    }

    private void fetchReviews(){
        final String BASE_URL = "http://api.themoviedb.org/3/movie/";

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        ThemoviedbAPI api = retrofit.create(ThemoviedbAPI.class);
        Call<ReviewsResponse> call = api.getReviews(movie.getmId());
        call.enqueue(new Callback<ReviewsResponse>(){

            @Override
            public void onResponse(Call<ReviewsResponse> call, Response<ReviewsResponse> response) {
                ArrayList<Review> result = (ArrayList<Review>) response.body().getReviews();
                if(result == null){
                    Toast.makeText(getActivity(), R.string.error_network, Toast.LENGTH_LONG).show();
                    return;
                }
                movie.setmReviews(result);
                setupReviewsList();
            }

            @Override
            public void onFailure(Call<ReviewsResponse> call, Throwable t) {
                Toast.makeText(getActivity(), R.string.error_network, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupReviewsList() {
        reviewsRv.setLayoutManager(new LinearLayoutManager(reviewsRv.getContext()));
        if (movie.getmReviews() == null) {
            return;
        }
        reviewsRv.setAdapter(new SimpleRecyclerViewAdapter(getContext(),
                movie.getmReviews()));
        reviewsRv.setNestedScrollingEnabled(false);
    }


    public static class SimpleRecyclerViewAdapter extends
            RecyclerView.Adapter<ReviewsFragment.SimpleRecyclerViewAdapter.ViewHolder>{

        private List<Review> mReviews;

        SimpleRecyclerViewAdapter(Context context, List<Review> reviews) {
            mReviews = reviews;
        }

        static class ViewHolder extends RecyclerView.ViewHolder{
            Review mBoundReview;
            final View mView;
            @BindView(R.id.author_name_tv) TextView mAuthorNameTv;
            @BindView(R.id.content_tv) TextView mReviewContentTv;
            int mPosition;
            Context mContext;

            ViewHolder(View view) {
                super(view);
                mView = view;
                mContext = view.getContext();
                ButterKnife.bind(this, view);
            }
        }

        @Override
        public ReviewsFragment.SimpleRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.reviews_list_item, parent, false);
            return new ReviewsFragment.SimpleRecyclerViewAdapter.ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull final ReviewsFragment.SimpleRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.mPosition = holder.getAdapterPosition();
            holder.mBoundReview = getItem(holder.mPosition);
            holder.mAuthorNameTv.setText(holder.mBoundReview.getAuthor());
            holder.mReviewContentTv.setText(holder.mBoundReview.getContent());
        }

        @Override
        public int getItemCount() {
            return mReviews.size();
        }

        Review getItem(int position){
            return mReviews.get(position);
        }

    }
}

