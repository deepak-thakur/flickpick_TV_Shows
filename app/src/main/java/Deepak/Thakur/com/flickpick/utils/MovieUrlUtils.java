package Deepak.Thakur.com.flickpick.utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import Deepak.Thakur.com.flickpick.BuildConfig;


public class MovieUrlUtils {

    public static final String API_KEY = BuildConfig.API_KEY;
    public static final String page ="page";

    private static final String LOG_TAG = MovieUrlUtils.class.getSimpleName();
    private static final String MOVIE_QUERY_API = "api_key";
    private static final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/tv/";


    static URL buildUrl(String... params) {
        String movieUrl = params[0];
        String page1 = params[1];
        if(params.length > 1 && params[1]!= null) {
           page1  = params[1];
        }

        Uri uri = Uri.parse(MOVIE_BASE_URL)
                .buildUpon()
                .appendPath(movieUrl)
                .appendQueryParameter(MOVIE_QUERY_API, API_KEY)
                .appendQueryParameter(page,page1)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problems create url", e);
        }

        return url;
    }
    public static URL buildUrlTrailers(String idTrailer, String trailer){
        Uri uri = Uri.parse(MOVIE_BASE_URL.concat(idTrailer.concat(trailer)))
                .buildUpon()
                .appendQueryParameter(MOVIE_QUERY_API, API_KEY)
                .build();
        URL url= null;
        try {
            url = new URL(uri.toString());
        }catch (MalformedURLException e){
            Log.e(LOG_TAG, "Problems create url", e);
        }
        return url;
    }
    public static URL buildUrlReview( String idMovie, String reviews){
        Uri uri = Uri.parse(MOVIE_BASE_URL.concat(idMovie).concat(reviews))
                .buildUpon()
                .appendQueryParameter(MOVIE_QUERY_API, API_KEY)
                .build();
        URL url= null;
        try {
            url= new URL(uri.toString());

        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problems create url", e);
        }

        return url;
    }


   public static String getResponseFromHttp(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = urlConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
