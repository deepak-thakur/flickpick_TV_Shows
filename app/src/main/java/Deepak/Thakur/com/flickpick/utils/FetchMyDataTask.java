package Deepak.Thakur.com.flickpick.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import java.net.URL;

import Deepak.Thakur.com.flickpick.R;
import Deepak.Thakur.com.flickpick.interfaces.AsynTaskCompleteListeningMovie;
import Deepak.Thakur.com.flickpick.model.Movie;
import Deepak.Thakur.com.flickpick.ui.MainActivity;

import static Deepak.Thakur.com.flickpick.ui.MainActivity.btn_retry;
import static Deepak.Thakur.com.flickpick.ui.MainActivity.tv_error;


public class FetchMyDataTask extends AsyncTask<String, Void, Movie[]> {



    @SuppressLint("StaticFieldLeak")
    private Context mContext;
    private AsynTaskCompleteListeningMovie mListener;



    public FetchMyDataTask(Context context, AsynTaskCompleteListeningMovie listener){
        this.mContext =context;
        this.mListener = listener;


    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        MainActivity.preExecute();
    }
    @Override
    protected Movie[] doInBackground(String... strings) {
        Movie[] mMovie = null;
        String usePage=strings[1];
        if (!CheckIfOnline()) {
            MainActivity.errorNetworkApi();
            return null;
        }
        if (MovieUrlUtils.API_KEY.equals("")) {
            MainActivity.errorNetworkApi();
            tv_error.setText(R.string.missing_api_key);
            tv_error.setTextColor(ContextCompat.getColor(mContext, R.color.secondary_text));
            btn_retry.setVisibility(View.INVISIBLE);
            return null;
        }
//        if(strings.length == 1 && strings[0]!= null) {
//            usePage  = "";
////            Log.d("name deepak",strings[1]);
//        }

        URL movieUrl = MovieUrlUtils.buildUrl(strings[0],usePage);
        Log.d("game",strings[0]);



        String movieResponse;
        try {
            movieResponse = MovieUrlUtils.getResponseFromHttp(movieUrl);
            mMovie = MovieJsonUtils.parseJsonMovieForMain(movieResponse);
        } catch (Exception e) {

            e.printStackTrace();
        }

        return mMovie;
    }

    @Override
    protected void onPostExecute(Movie[] movies) {
        super.onPostExecute(movies);
        mListener.onTaskComplete(movies);

    }



    private boolean CheckIfOnline() {
        ConnectivityManager connectionM = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectionM != null;
        NetworkInfo netInfo = connectionM.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


}
