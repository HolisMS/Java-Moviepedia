package satella.app.movies4.database;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import satella.app.movies4.model.Movie;

@Dao
public interface MovieDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Movie movies);

    @Query("SELECT * FROM movie")
    Cursor selectAll();

    @Query("SELECT * FROM movie where uid = :uid")
    Cursor selectById(long uid);

    @Query("SELECT * FROM movie")
    List<Movie> getAllFavMovies();

    @Query("DELETE FROM movie WHERE uid = :uid")
    void deleteByUid(int uid);

    @Query("SELECT COUNT(uid) FROM movie WHERE originalTitle = :title")
    int getMovieByTitle(String title);

    @Query("SELECT * FROM movie WHERE movieType = :movieType")
    List<Movie> getMoviesByMovieType(String movieType);

}
