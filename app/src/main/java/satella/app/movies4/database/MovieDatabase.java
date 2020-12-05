package satella.app.movies4.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import satella.app.movies4.model.Movie;

@Database(entities = {Movie.class}, version = 1)
public abstract class MovieDatabase extends RoomDatabase {
    public abstract MovieDAO getMovieDAO();
}
