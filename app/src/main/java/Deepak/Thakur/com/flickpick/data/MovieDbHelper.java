package Deepak.Thakur.com.flickpick.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movieDb.db";
    private static final int VERSION = 1;

    MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TableName + " (" +
                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                MovieContract.MovieEntry.ColumnID + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.ColumnTitle + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.ColumnImage + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.ColumnPlot + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.ColumnRating + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.ColumnRelease + " TEXT NOT NULL);";

        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TableName);
        onCreate(db);
    }
}
