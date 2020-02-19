package Deepak.Thakur.com.flickpick.ui;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;

import Deepak.Thakur.com.flickpick.R;
import Deepak.Thakur.com.flickpick.adapter.ReviewAdapter;
import Deepak.Thakur.com.flickpick.adapter.TrailerAdapter;
import Deepak.Thakur.com.flickpick.data.MovieContract;
import Deepak.Thakur.com.flickpick.databinding.ActivityDetailBinding;
import Deepak.Thakur.com.flickpick.model.Movie;
import Deepak.Thakur.com.flickpick.model.Review;
import Deepak.Thakur.com.flickpick.model.Trailer;
import Deepak.Thakur.com.flickpick.utils.MovieJsonUtils;
import Deepak.Thakur.com.flickpick.utils.MovieUrlUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

import static Deepak.Thakur.com.flickpick.ui.MainActivity.btn_retry;
import static Deepak.Thakur.com.flickpick.ui.MainActivity.isFavorited;
import static Deepak.Thakur.com.flickpick.ui.MainActivity.mContext;
import static Deepak.Thakur.com.flickpick.ui.MainActivity.tv_error;


public class DetailActivity extends AppCompatActivity  {


    ActivityDetailBinding mBinding;
    @BindView(R.id.rv_reviews)
    RecyclerView mRecyclerViewReviews;
    @BindView(R.id.rv_trailers)
    RecyclerView mRecyclerViewTrailers;
    @BindView(R.id.fb_detail)
    FloatingActionButton fb;

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();
    private static final String IMAGE__URL_PATH = "http://image.tmdb.org/t/p/w342";
    private static final String IMAGE_URL_PATH_LARGE_POSTER = "http://image.tmdb.org/t/p/w780";
    public static Movie ReceivedMovieData;
    private Review[] mReviews = null;
    private Trailer[] mTrailers = null;
    public static ArrayList<String[]> arrayListMovies = new ArrayList<>();
    public static ArrayList<String> dataDetail = new ArrayList<>();
    private String MovieTitle, MoviePoster, MoviePlot, MovieRating, release, releaseFinal, id;
    public static String [][] MovieFav;
    public static boolean FromFavouriteMovies;
    int position, idToast;
    private final String YOUTUBE_URL = "http://www.youtube.com/watch?v=";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        ButterKnife.bind(this);

        // the snap helper is used to scroll the items of recyclerview 1 by 1
        // you have to add external library to use gravity snaphelper
        SnapHelper snapHelper = new GravitySnapHelper(Gravity.START);
        snapHelper.attachToRecyclerView(mRecyclerViewTrailers);

        SnapHelper snapHelper2 = new GravitySnapHelper(Gravity.START);
        snapHelper2.attachToRecyclerView(mRecyclerViewReviews);

        mRecyclerViewReviews.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerViewTrailers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerViewReviews.setHasFixedSize(true);
        mRecyclerViewTrailers.setHasFixedSize(true);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        FromFavouriteMovies= bundle.getBoolean("FromFavouriteMovies");
        position = bundle.getInt("sendPosition");

        if (!FromFavouriteMovies){
            MoviesPopulate();
        }else {
           PopulateFavouriteMovies();
        }

        new MovieFetchTaskReviews().execute("reviews");
        new MoviewFetchTaskTrailer().execute("videos");

    }

    public void MoviesPopulate(){
        ReceivedMovieData = getIntent().getParcelableExtra("sendData");
        MovieTitle = ReceivedMovieData.getmTitle();
        MoviePoster = ReceivedMovieData.getmMoviePoster();
        MoviePlot = ReceivedMovieData.getmPlot();
        MovieRating = ReceivedMovieData.getmRating();
        release = ReceivedMovieData.getmReleaseDate();
        releaseFinal = release.substring(0, 4);
        fb.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite));
        Picasso.with(this)
                .load(IMAGE__URL_PATH.concat(MoviePoster))
                .into(mBinding.ivPosterDetail);

        putData();
    }

    public void PopulateFavouriteMovies(){
        fb.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_delete));
        Picasso.with(this)
                .load(MovieFav[position][1])
                .into(mBinding.ivPosterDetail);
        MovieTitle= MovieFav[position][0];
        MoviePlot= MovieFav[position][4];
        MovieRating= MovieFav[position][3];
        release = MovieFav[position][2];
        releaseFinal = release.substring(0, 4);
        id= MovieFav[position][5];
        putData();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void toZoom(View view){
        String urlImageLarge;
        if (!FromFavouriteMovies){
            urlImageLarge = IMAGE_URL_PATH_LARGE_POSTER.concat(MoviePoster);
        }else {
            urlImageLarge = MovieFav[position][1];
        }
        Intent toZoom = new Intent(this, ZoomPoster.class);
        toZoom.putExtra("poster", urlImageLarge);
        toZoom.putExtra("title", MovieTitle);
        startActivity(toZoom);
    }

    public void putData(){
        mBinding.tvTitle.setText(MovieTitle);
        mBinding.tvPlot.setText(MoviePlot);
        mBinding.tvRating.setText(MovieRating);
        mBinding.tvRelease.setText(releaseFinal);
        setTitle(MovieTitle);

        dataDetail.add(getTitle().toString());
        dataDetail.add(MoviePoster);
        dataDetail.add(MoviePlot);
        dataDetail.add(MovieRating);
        dataDetail.add(releaseFinal);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void saveDeleteData(View view){

        if (!isFavorited() && !FromFavouriteMovies) {
            idToast = 2;
            MovietoastMessages();
            return;
        }
        if (!isFavorited()){
            idToast=1;
            deleteMovieData();
            if (FromFavouriteMovies)fb.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite));
        }else {
            idToast=0;
            saveMovieData();
            if (FromFavouriteMovies)fb.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_delete));
        }
    }
    private void MovietoastMessages(){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast, (ViewGroup)findViewById(R.id.toast_container));
        TextView tv_toast = layout.findViewById(R.id.tv_toast);

        switch (idToast){
            case 0:
                tv_toast.setText(getResources().getString(R.string.toast_save));
                break;
            case 1:
                tv_toast.setText(getResources().getString(R.string.toast_delete));
                break;
            case 2:
                tv_toast.setText(getResources().getString(R.string.toast_in_favorites));
                break;

        }
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }



    public boolean isFavorited(){

        Cursor mCursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                MovieContract.MovieEntry.ColumnID);
        if (mCursor != null){
            while (mCursor.moveToNext()){
                String idMovies = mCursor.getString(1);
                String idMovieActual;
                if (!FromFavouriteMovies){
                     idMovieActual= String.valueOf(ReceivedMovieData.getmId());
                }else {
                    idMovieActual= MovieFav[position][5];
                }
                if (idMovies.equals(idMovieActual)){
                    return false;
                }
            }
        }
        assert mCursor != null;
        mCursor.close();
        return true;
    }

    private void deleteMovieData() {
        ContentResolver contentResolver = getContentResolver();
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(String.valueOf(id)).build();

        contentResolver.delete(uri, null, null);
        MainActivity.dataDetail.clear();
        idToast=1;
        MovietoastMessages();

    }

    private void saveMovieData() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry.ColumnID, ReceivedMovieData.getmId());
        contentValues.put(MovieContract.MovieEntry.ColumnTitle, ReceivedMovieData.getmTitle());
        contentValues.put(MovieContract.MovieEntry.ColumnImage, IMAGE_URL_PATH_LARGE_POSTER.concat(ReceivedMovieData.getmMoviePoster()));
        contentValues.put(MovieContract.MovieEntry.ColumnPlot, ReceivedMovieData.getmPlot());
        contentValues.put(MovieContract.MovieEntry.ColumnRating, ReceivedMovieData.getmRating());
        contentValues.put(MovieContract.MovieEntry.ColumnRelease, ReceivedMovieData.getmReleaseDate());
        getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
        idToast=0;
        MovietoastMessages();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);
        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                switch (MainActivity.queryMovie) {
                    case "popular":
                        MainActivity.queryMovie = "popular";
                        break;
                    case "top_rated":
                        MainActivity.queryMovie = "top_rated";
                        break;
                }
                break;
            case R.id.menu_share:
               Log.e(LOG_TAG, mTrailers[position].getmKey());
                Intent shareMovieVideo = new Intent(Intent.ACTION_SEND);
                shareMovieVideo.setType("text/plain");
                if (!isFavorited){
                    shareMovieVideo.putExtra(Intent.EXTRA_SUBJECT, ReceivedMovieData.getmTitle());
                    shareMovieVideo.putExtra(Intent.EXTRA_TEXT, ReceivedMovieData.getmTitle() + " " + YOUTUBE_URL.concat(mTrailers[position].getmKey()));
                }else {
                    shareMovieVideo.putExtra(Intent.EXTRA_SUBJECT, MovieFav[position][0]);
                    shareMovieVideo.putExtra(Intent.EXTRA_TEXT, MovieFav[position][0] + " " + YOUTUBE_URL.concat(mTrailers[position].getmKey()));
                }
                startActivity(Intent.createChooser(shareMovieVideo, getString(R.string.share_trailer)));

                break;
        }
        return super.onOptionsItemSelected(item);
    }




    @SuppressLint("StaticFieldLeak")
    private class MoviewFetchTaskTrailer extends AsyncTask<String, Void, Trailer[]> {

        URL trailerUrl;
        @Override
        protected Trailer[] doInBackground(String... strings) {

            if (MovieUrlUtils.API_KEY.equals("")) {
                MainActivity.errorNetworkApi();
                tv_error.setText(R.string.missing_api_key);
                tv_error.setTextColor(ContextCompat.getColor(mContext, R.color.secondary_text));
                btn_retry.setVisibility(View.INVISIBLE);
                return null;
            }
            if (!FromFavouriteMovies){
                trailerUrl= MovieUrlUtils.buildUrlTrailers(String.valueOf(ReceivedMovieData.getmId()).concat("/"), strings[0]);
            }else {
                trailerUrl= MovieUrlUtils.buildUrlTrailers(id.concat("/"), strings[0]);

            }
            String trailerResponse;
            try {
                trailerResponse = MovieUrlUtils.getResponseFromHttp(trailerUrl);
                mTrailers = MovieJsonUtils.parseJsonMovieTrailer(trailerResponse);
            } catch (Exception e) {
                Log.e(LOG_TAG, "Problems with trailer", e);
            }

            return mTrailers;
        }

        @Override
        protected void onPostExecute(Trailer[] trailers) {
            if (trailers != null) {
                mTrailers = trailers;
                TrailerAdapter trailerAdapter = new TrailerAdapter(mTrailers, DetailActivity.this);
                mRecyclerViewTrailers.setAdapter(trailerAdapter);

            } else {
                Log.e(LOG_TAG, "Problems with adapter");
            }

            if (isCancelled()) {
                new MoviewFetchTaskTrailer().cancel(true);
            }
            if(mTrailers !=null)
                for (Trailer mTrailer : mTrailers) {
                    if(!mTrailer.getmName().equals("NA") && !mTrailer.getmKey().equals("NA")) {
                        dataDetail.add(mTrailer.getmName());
                        dataDetail.add(mTrailer.getmKey());
                    }

                }
            assert trailers != null;

            if (trailers != null && Array.getLength(trailers) == 0) {
                mRecyclerViewTrailers.setVisibility(View.INVISIBLE);
                mBinding.tvAdapterNoDataReview.setVisibility(View.VISIBLE);

            }
        }

        @Override
        protected void onCancelled(Trailer[] trailers) {
            super.onCancelled(trailers);
            new MoviewFetchTaskTrailer().cancel(true);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class MovieFetchTaskReviews extends AsyncTask<String, Void, Review[]> {



        @Override
        protected Review[] doInBackground(String... strings) {

            URL reviewUrl;
            if (MovieUrlUtils.API_KEY.equals("")) {
                MainActivity.errorNetworkApi();
                tv_error.setText(R.string.missing_api_key);
                tv_error.setTextColor(ContextCompat.getColor(mContext, R.color.secondary_text));
                btn_retry.setVisibility(View.INVISIBLE);
                return null;
            }

            if (!FromFavouriteMovies){
                 reviewUrl= MovieUrlUtils.buildUrlReview(String.valueOf(ReceivedMovieData.getmId()).concat("/"), strings[0]);
            }else {
                reviewUrl= MovieUrlUtils.buildUrlReview(id.concat("/"), strings[0]);

            }
            String reviewResponse;
            try {
                reviewResponse = MovieUrlUtils.getResponseFromHttp(reviewUrl);
                mReviews = MovieJsonUtils.parseJsonMovieReview(reviewResponse);

            } catch (Exception e) {
                Log.e(LOG_TAG, "Problems with review", e);
            }

            return mReviews;
        }

        @Override
        protected void onPostExecute(Review[] reviews) {
            if (reviews != null) {
                mReviews = reviews;
                ReviewAdapter reviewAdapter = new ReviewAdapter(mReviews);
                mRecyclerViewReviews.setAdapter(reviewAdapter);

            } else {
                Log.e(LOG_TAG, "Problems with adapter");
            }
            if (isCancelled()) {
                new MovieFetchTaskReviews().cancel(true);
            }
            for (Review mReview : mReviews){
                dataDetail.add(mReview.getmAuthor());
                dataDetail.add(mReview.getmContent());
            }

            assert reviews != null;
            if (reviews.length == 0) {
                mRecyclerViewReviews.setVisibility(View.INVISIBLE);
                mBinding.tvAdapterNoData.setVisibility(View.VISIBLE);
                mBinding.tvAdapterNoData.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.secondary_text));

            }

        }

        @Override
        protected void onCancelled(Review[] reviews) {
            super.onCancelled(reviews);
            new MovieFetchTaskReviews().cancel(true);
        }
    }
}
