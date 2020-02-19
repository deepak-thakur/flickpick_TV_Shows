package Deepak.Thakur.com.flickpick.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Deepak.Thakur.com.flickpick.R;
import Deepak.Thakur.com.flickpick.ui.DetailActivity;


public class MovieAdapterFavorites extends RecyclerView.Adapter<MovieAdapterFavorites.MovieHolder>{

    private Context mContext=null;
    private MovieClickListener MmovieClickListener=null;

    public MovieAdapterFavorites(ArrayList<String[]> arrayList, Context context, MovieClickListener movieClickListener){
        DetailActivity.arrayListMovies = arrayList;
        mContext = context;
        MmovieClickListener= movieClickListener;

    }

    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_movie_poster, parent, false);

        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieHolder holder, int position) {

        Picasso.with(mContext)
                .load(DetailActivity.MovieFav[position][1])
                .into(holder.ImageViewHolder);

    }


    @Override
    public int getItemCount() {
        return DetailActivity.MovieFav.length;
    }

    public interface MovieClickListener {
        void onClickMovie(int position);
    }

    class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final ImageView ImageViewHolder;

        MovieHolder(View itemView) {
            super(itemView);
            ImageViewHolder = itemView.findViewById(R.id.IV_list_item_poster);
            ImageViewHolder.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickPosition = getAdapterPosition();
            MmovieClickListener.onClickMovie(clickPosition);
        }
    }
}
