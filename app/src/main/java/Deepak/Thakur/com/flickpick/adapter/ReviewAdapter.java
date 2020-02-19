package Deepak.Thakur.com.flickpick.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import Deepak.Thakur.com.flickpick.R;
import Deepak.Thakur.com.flickpick.model.Review;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder>  {

    private Review[] mMovieReview;
    private TextView Tv_Author_Adapter;
    private TextView Tv_Review_Adapter;



    public ReviewAdapter(Review[] review) {
        this.mMovieReview = review;

    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_review, parent, false);

        return new ReviewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ReviewHolder holder, int position) {
        String author= mMovieReview[position].getmAuthor();
        String review= mMovieReview[position].getmContent();
        Tv_Author_Adapter.setText(author);
        Tv_Review_Adapter.setText(review);

    }

    @Override
    public int getItemCount() {

        return mMovieReview.length;
    }

    class ReviewHolder extends RecyclerView.ViewHolder {

        ReviewHolder(View itemView) {
            super(itemView);
            Tv_Author_Adapter = itemView.findViewById(R.id.tv_author);
            Tv_Review_Adapter = itemView.findViewById(R.id.tv_review_adapter);
        }
    }
}
