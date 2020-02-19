package Deepak.Thakur.com.flickpick.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import Deepak.Thakur.com.flickpick.R;

import static Deepak.Thakur.com.flickpick.data.MovieContract.MovieEntry.TableName;




public class MovieContentProvider extends ContentProvider {

    public static final int MOVIE = 101;
    public static final int MOVIES_ID = 102;

    private static final UriMatcher Matcher_Uri = buildUriMatcher();

    public  static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MovieContract.AUTHORITY_Name, MovieContract.Movie_Path, MOVIE);
        uriMatcher.addURI(MovieContract.AUTHORITY_Name, MovieContract.Movie_Path + "/#", MOVIES_ID);

        return uriMatcher;
    }
    private MovieDbHelper mMovieDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mMovieDbHelper = new MovieDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        int match = Matcher_Uri.match(uri);
        Uri returnUri;

        switch (match){
            case MOVIE:
                long id= db.insert(MovieContract.MovieEntry.TableName, null, values);

                if (id > 0){
                    returnUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id);
                }else throw new SQLException(getContext().getString(R.string.failed_insert_row) + uri);
                break;
                default:
                    throw new UnsupportedOperationException(getContext().getString(R.string.unknow_uri) + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mMovieDbHelper.getReadableDatabase();
        int match = Matcher_Uri.match(uri);
        Cursor retCursor;

        switch (match){
            case MOVIE:
                retCursor = db.query(TableName,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
                default:
                    throw new UnsupportedOperationException(getContext().getString(R.string.unknow_uri) + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }



    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        int match = Matcher_Uri.match(uri);
        int movieDeleted;

        switch (match){
            case MOVIES_ID:
                String id = uri.getPathSegments().get(1);
                movieDeleted = db.delete(TableName, MovieContract.MovieEntry.ColumnID +"=?", new String[]{id});
                break;
                default:
                    throw new UnsupportedOperationException(getContext().getString(R.string.unknow_uri) + uri);
        }
        if (movieDeleted != 0){

            getContext().getContentResolver().notifyChange(uri, null);
        }
        return  movieDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
