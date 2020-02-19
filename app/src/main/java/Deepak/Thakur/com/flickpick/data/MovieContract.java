package Deepak.Thakur.com.flickpick.data;

import android.net.Uri;
import android.provider.BaseColumns;



public class MovieContract {

    static final String AUTHORITY_Name = "Deepak.Thakur.com.flickpick";
    static final Uri BASE_CONTENT_URI_Name = Uri.parse("content://" + AUTHORITY_Name);
    static final String Movie_Path = "movies";

    public static final class MovieEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI_Name.buildUpon()
                        .appendPath(Movie_Path)
                        .build();

        static final String TableName = "movies";
        public static final String ColumnID = "id";
        public static final String ColumnTitle = "title";
        public static final String ColumnImage = "image";
        public static final String ColumnPlot = "plot";
        public static final String ColumnRating = "rating";
        public static final String ColumnRelease = "release";


    }

}
