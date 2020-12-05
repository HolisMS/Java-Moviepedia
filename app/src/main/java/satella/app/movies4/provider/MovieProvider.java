package satella.app.movies4.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.Room;

import satella.app.movies4.database.MovieDAO;
import satella.app.movies4.database.MovieDatabase;

import static satella.app.movies4.utilities.Utils.DATABASE_NAME;

public class MovieProvider extends ContentProvider {

    private MovieDatabase database;
    private MovieDAO movieDAO;

    private static final String TABLE_NAME = "movie";
    private static final String AUTHORITY = "satella.app.movies4.provider";
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int CODE_FAV_DIR = 1;
    private static final int CODE_FAV_ITEM = 2;

    static {
        uriMatcher.addURI(AUTHORITY, TABLE_NAME, CODE_FAV_DIR);
        uriMatcher.addURI(AUTHORITY, TABLE_NAME + "/#", CODE_FAV_ITEM);
    }

    public MovieProvider() {
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public boolean onCreate() {
        database = Room.databaseBuilder(getContext(), MovieDatabase.class, DATABASE_NAME).build();
        movieDAO = database.getMovieDAO();
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final int code = uriMatcher.match(uri);
        if (code == CODE_FAV_DIR || code == CODE_FAV_ITEM) {
            final Context context = getContext();
            if (context == null)
                return null;
            final Cursor cursor;
            if (code == CODE_FAV_DIR)
                cursor = movieDAO.selectAll();
            else
                cursor = movieDAO.selectById(ContentUris.parseId(uri));
            cursor.setNotificationUri(context.getContentResolver(), uri);
            return cursor;
        } else {
            throw new IllegalArgumentException("Unknown Uri : " + uri);

        }

    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        return 0;
    }
}
