package Deepak.Thakur.com.flickpick.utils;

import android.app.Application;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Deepak.Thakur.com.flickpick.model.Movie;
import Deepak.Thakur.com.flickpick.model.Review;
import Deepak.Thakur.com.flickpick.model.Trailer;


public class MovieJsonUtils extends Application{




    private static final String MOVIE_RESULTS = "results";
    private static final String MOVIE_ID = "id";
    private static final String MOVIE_TITLE = "name";
    private static final String MOVIE_POSTER = "poster_path";
    private static final String MOVIE_PLOT = "overview";
    private static final String MOVIE_RATING = "vote_average";

    private static final String TOP_RATED_SHOWS = "top_rated";

    private static final String MOVIE_RELEASE_DATE = "first_air_date";

    private static final String MOVIE_REVIEW_AUTHOR = "author";
    private static final String MOVIE_REVIEW_CONTENT = "content";

    private static final String MOVIE_TRAILER_RESULTS = "results";
    private static final String MOVIE_TRAILER_NAME = "name";
    private static final String MOVIE_TRAILER_SOURCE = "key";



    static Movie[] parseJsonMovieForMain(String jsonMoviesData) throws JSONException {

        JSONObject jsonRoot = new JSONObject(jsonMoviesData);
        JSONArray jsonArrayResult = jsonRoot.getJSONArray(MOVIE_RESULTS);
        Movie[] result = new Movie[jsonArrayResult.length()];
        for (int i = 0; i < jsonArrayResult.length(); i++) {
            Movie movie = new Movie();
            movie.setmId(jsonArrayResult.getJSONObject(i).optString(MOVIE_ID));
            movie.setmTitle(jsonArrayResult.getJSONObject(i).optString(MOVIE_TITLE));
            movie.setmMoviePoster(jsonArrayResult.getJSONObject(i).optString(MOVIE_POSTER));
            movie.setmPlot(jsonArrayResult.getJSONObject(i).optString(MOVIE_PLOT));
            movie.setmRating(jsonArrayResult.getJSONObject(i).optString(MOVIE_RATING));
            movie.setmShows(jsonArrayResult.getJSONObject(i).optString(TOP_RATED_SHOWS));

            movie.setmReleaseDate(jsonArrayResult.getJSONObject(i).optString(MOVIE_RELEASE_DATE));
            result[i] = movie;
        }
        return result;
    }

    public static Review[] parseJsonMovieReview(String jsonReviewData) throws JSONException{

        JSONObject jsonRoot = new JSONObject(jsonReviewData);
        JSONArray jsonArrayResult = jsonRoot.getJSONArray(MOVIE_RESULTS);
        Review[] result = new Review[jsonArrayResult.length()];
        for (int i=0; i < jsonArrayResult.length(); i++){
            Review review = new Review();
            review.setmAuthor(jsonArrayResult.getJSONObject(i).optString(MOVIE_REVIEW_AUTHOR));
            review.setmContent(jsonArrayResult.getJSONObject(i).optString(MOVIE_REVIEW_CONTENT));
            result[i] = review;
        }
        return result;
    }

    public static Trailer[] parseJsonMovieTrailer(String jsonTrailerData) throws JSONException{

        JSONObject jsonRoot = new JSONObject(jsonTrailerData);
        try {


            JSONArray jsonArrayResult = jsonRoot.getJSONArray(MOVIE_TRAILER_RESULTS);
            Trailer[] result = new Trailer[jsonArrayResult.length()];
            for (int i = 0; i < jsonArrayResult.length(); i++) {
                Trailer trailer = new Trailer();
                trailer.setmKey(jsonArrayResult.getJSONObject(i).optString(MOVIE_TRAILER_SOURCE));
                trailer.setmName(jsonArrayResult.getJSONObject(i).optString(MOVIE_TRAILER_NAME));
                result[i] = trailer;

            }
            return result;

        }catch(JSONException e){
            Trailer[] result = new Trailer[1];
            Trailer trailer = new Trailer();

            trailer.setmKey("NA");
            trailer.setmName("NA");
            result[0] = trailer;
            return result;

        }
    }


}
