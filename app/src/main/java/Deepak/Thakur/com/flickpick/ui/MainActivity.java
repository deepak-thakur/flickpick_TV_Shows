package Deepak.Thakur.com.flickpick.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import Deepak.Thakur.com.flickpick.R;
import Deepak.Thakur.com.flickpick.adapter.MovieAdapter;
import Deepak.Thakur.com.flickpick.adapter.MovieAdapterFavorites;
import Deepak.Thakur.com.flickpick.data.MovieContract;
import Deepak.Thakur.com.flickpick.interfaces.AsynTaskCompleteListeningMovie;
import Deepak.Thakur.com.flickpick.model.Movie;
import Deepak.Thakur.com.flickpick.utils.FetchMyDataTask;
import Deepak.Thakur.com.flickpick.utils.MovieUrlUtils;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieClickListener,
            MovieAdapterFavorites.MovieClickListener{

    private static final String CALLBACK_QUERY = "callbackQuery";
    private static final String CALLBACK_NAMESORT= "callbackNamesort";
    private static final String CALLBACK_FAVORITES = "callbackFavorites";
    public static String queryMovie = "popular";
    private String nameSort = "Popular Movies";
    public static Movie[] mMovie = null;
    public static boolean isFavorited;
    private MovieAdapterFavorites movieAdapter;
    public static ArrayList<String> dataDetail = new ArrayList<>();
    @SuppressLint("StaticFieldLeak")
    public static Context mContext;
    @SuppressLint("StaticFieldLeak")
    public static RecyclerView mRecyclerView;
    @SuppressLint("StaticFieldLeak")
    public static ProgressBar progressBar;
    @SuppressLint("StaticFieldLeak")
    public static TextView tv_error;
    @SuppressLint("StaticFieldLeak")
    public static Button btn_retry;
    @SuppressLint("StaticFieldLeak")
    public static TextView tv_no_data;

    Boolean isScrolling = false;
    int currentItem,scrolledItem,totalItems;
//    LinearLayoutManager manager;
    GridLayoutManager gridmanager;
    int page = 1;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.rv_main);
        btn_retry = findViewById(R.id.btn_retry);
        gridmanager = new GridLayoutManager(this, 3);

        mRecyclerView.setLayoutManager(gridmanager);
        mRecyclerView.setHasFixedSize(true);

        progressBar = findViewById(R.id.pb_main);
        tv_error = findViewById(R.id.tv_error);
        tv_no_data = findViewById(R.id.tv_no_data);
        tv_no_data.setVisibility(View.INVISIBLE);
        mContext = getApplicationContext();

//        manager = new LinearLayoutManager(this);

         mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
             @Override
             public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                 super.onScrollStateChanged(recyclerView, newState);

                 if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                     isScrolling = true;
                 }
             }

             @Override
             public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                 super.onScrolled(recyclerView, dx, dy);
                 currentItem = gridmanager.getChildCount();
                         scrolledItem = gridmanager.findFirstVisibleItemPosition();
                         totalItems = gridmanager.getItemCount();
                    if(isScrolling && ( currentItem + scrolledItem ==totalItems)){
                            isScrolling = false;
                            queryMovie="popular";
                            page = page + 1;
                        new FetchMyDataTask(getApplicationContext(), new MovieFetchTaskCompleteListener()).execute(queryMovie,String.valueOf(page));


                    }
             }
         });

        setTitle(nameSort);
        if (!CheckIfOnline()) {
            errorNetworkApi();
            return;
        }

        if (savedInstanceState != null){
            if (savedInstanceState.containsKey(CALLBACK_FAVORITES)){
                nameSort= savedInstanceState.getString(CALLBACK_FAVORITES);
                queryMovie = "favorites";
                setTitle(nameSort);
                loadDataFavorites();
                return;
            }
            if (savedInstanceState.containsKey(CALLBACK_QUERY) || savedInstanceState.containsKey(CALLBACK_NAMESORT)){
                queryMovie = savedInstanceState.getString(CALLBACK_QUERY);
                nameSort = savedInstanceState.getString(CALLBACK_NAMESORT);
                setTitle(nameSort);
//                page = page + 1;
                new FetchMyDataTask(this, new MovieFetchTaskCompleteListener()).execute(queryMovie);
                return;
            }
        }
        if (nameSort.equals("Favorites"))return;
        page = page+1;
        new FetchMyDataTask(this, new MovieFetchTaskCompleteListener()).execute(queryMovie,String.valueOf(page));

    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public void loadDataFavorites(){
        loadFavorites();
        movieAdapter = new MovieAdapterFavorites(DetailActivity.arrayListMovies, this, MainActivity.this);
        movieAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(movieAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (isFavorited){
            String nameSortSaved = nameSort;
            outState.putString(CALLBACK_FAVORITES, nameSortSaved);
            return;
        }
        String queryMovieSaved = queryMovie;
        String nameSortSaved = nameSort;
        outState.putString(CALLBACK_QUERY, queryMovieSaved);
        outState.putString(CALLBACK_NAMESORT, nameSortSaved);


    }
    private boolean CheckIfOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!CheckIfOnline()) return false;
        if (MovieUrlUtils.API_KEY.equals("")) return false;
        int id = item.getItemId();
        switch (id) {
            case R.id.popularity:
                isFavorited=false;
                tv_no_data.setVisibility(View.INVISIBLE);
                queryMovie = "popular";
                page= page + 1;
                new FetchMyDataTask(this, new MovieFetchTaskCompleteListener()).execute(queryMovie,String.valueOf(page));
                nameSort = "Popular Movies";
                setTitle(nameSort);
                break;
            case R.id.top_rated:
                isFavorited=false;
                tv_no_data.setVisibility(View.INVISIBLE);
                queryMovie = "top_rated";
                page=page+1;
                new FetchMyDataTask(this, new MovieFetchTaskCompleteListener()).execute(queryMovie,String.valueOf(page));
                nameSort = "Top Rated Movies";
                setTitle(nameSort);
                break;
            case R.id.favorites:
                isFavorited= true;
                nameSort= "Favorites";
                queryMovie = "favorites";
                loadFavorites();
                setTitle(nameSort);
                break;

            case R.id.tv_shows:
                isFavorited=false;
                nameSort="TV Show";
                queryMovie = "now_playing";
                page = page + 1;
                new FetchMyDataTask(this,new MovieFetchTaskCompleteListener()).execute(queryMovie,String.valueOf(page));
                setTitle(nameSort);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public  void loadFavorites() {
        dataDetail.clear();

        Cursor mCursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, MovieContract.MovieEntry._ID);

        if (mCursor != null){
            while (mCursor.moveToNext()){
                dataDetail.add(mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.ColumnImage)));
            }
        }
        assert mCursor != null;
        mCursor.close();
        loadData();

        mRecyclerView.setVisibility(View.VISIBLE);
        hideProgressAndTextview();

        movieAdapter = new MovieAdapterFavorites(DetailActivity.arrayListMovies, this, MainActivity.this);
        movieAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(movieAdapter);
        if (nameSort.equals("Favorites")){
            if (dataDetail.size()==0){
                tv_no_data.setVisibility(View.VISIBLE);
                tv_no_data.setTextColor(ContextCompat.getColor(this, R.color.secondary_text));
            }else {
                tv_no_data.setVisibility(View.INVISIBLE);
            }
        }


    }

    private void loadData() {
            DetailActivity.arrayListMovies.clear();
            DetailActivity.MovieFav=null;
            Cursor mCursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null
                    , null, null,
                    MovieContract.MovieEntry._ID);

            if (mCursor != null) {
                while (mCursor.moveToNext()) {
                    DetailActivity.arrayListMovies.add(new String[]{
                            mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.ColumnTitle)),
                            mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.ColumnImage)),
                            mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.ColumnRelease)),
                            mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.ColumnRating)),
                            mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.ColumnPlot)),
                            mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.ColumnID))});

                }
                DetailActivity.MovieFav= DetailActivity.arrayListMovies.toArray(new String[DetailActivity.arrayListMovies.size()][5]);
                mCursor.close();

        }
    }


    public static void errorNetworkApi() {
        progressBar.setVisibility(View.INVISIBLE);
        tv_error.setVisibility(View.VISIBLE);
        tv_error.setTextColor(ContextCompat.getColor(mContext, R.color.secondary_text));
        btn_retry.setVisibility(View.VISIBLE);
    }

    public static void preExecute(){
        mRecyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void clickRetry(View view) {
        if (!CheckIfOnline()) {
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            view.startAnimation(shake);
            return;
        }
        queryMovie = "popular";
        btn_retry.setVisibility(View.INVISIBLE);
        tv_error.setVisibility(View.INVISIBLE);
        new FetchMyDataTask(this, new MovieFetchTaskCompleteListener()).execute(queryMovie,String.valueOf(page));
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClickMovie(int position) {


        if (!CheckIfOnline()) {
            mRecyclerView.setVisibility(View.INVISIBLE);
            errorNetworkApi();
            return;
        }
        if (nameSort.equals("Favorites")){
            isFavorited=true;
            Intent intentToDetail = new Intent(this, DetailActivity.class);
            intentToDetail.putExtra("FromFavouriteMovies", isFavorited);
            intentToDetail.putExtra("sendPosition", position);
            startActivity(intentToDetail);
            isFavorited=false;
            return;
        }

        Intent intentToDetail = new Intent(this, DetailActivity.class);
        intentToDetail.putExtra("sendData",  mMovie[position]);
        startActivity(intentToDetail);
    }


    private void hideProgressAndTextview() {
        progressBar.setVisibility(View.INVISIBLE);
        tv_error.setVisibility(View.INVISIBLE);
    }





    private class MovieFetchTaskCompleteListener implements AsynTaskCompleteListeningMovie {

        @Override
        public void onTaskComplete(Movie[] result) {
            if (result != null) {
                mRecyclerView.setVisibility(View.VISIBLE);
                hideProgressAndTextview();
                mMovie = result;
//                MovieAdapter movieAdapter = new MovieAdapter(mMovie, MainActivity.this, MainActivity.this);
//                mRecyclerView.setLayoutManager(manager);
               MovieAdapter movieAdapter1 = new MovieAdapter(MainActivity.mMovie,MainActivity.this,MainActivity.this);
                movieAdapter1.notifyDataSetChanged();
                mRecyclerView.setAdapter(movieAdapter1);
            }
        }



    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPostResume() {
        super.onPostResume();
            if (!CheckIfOnline()){
            mRecyclerView.setVisibility(View.INVISIBLE);
            errorNetworkApi();
            return;
        }
        tv_no_data.setVisibility(View.INVISIBLE);
        if (DetailActivity.FromFavouriteMovies || isFavorited){
            isFavorited= true;
            nameSort= "Favorites";
            loadDataFavorites();
            setTitle(nameSort);
        }else if (queryMovie.equals("top_rated")){
            isFavorited=false;
            nameSort = "Top Rated Movies";
            setTitle(nameSort);
        }

    }

}
