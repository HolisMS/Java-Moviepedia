package satella.app.favoritemovie4;

import android.net.Uri;

public class Utils {
    public static final String TABLE_NAME = "movie";
    public static final String AUTHORITY = "satella.app.movies4.provider";
    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(AUTHORITY)
            .appendPath(TABLE_NAME)
            .build();
    public static final String BASE_URL_BACKDROP = "https://image.tmdb.org/t/p/w342";
    public static final String BASE_URL_POSTER = "https://image.tmdb.org/t/p/w185";
    public static final String COLUMN_TITLE = "originalTitle";
    public static final String COLUMN_VOTE = "voteAverage";
    public static final String COLUMN_BACKDROP = "backdropPath";
    public static final String COLUMN_POSTER = "posterPath";
}
