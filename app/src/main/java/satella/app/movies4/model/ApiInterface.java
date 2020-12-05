package satella.app.movies4.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("tv/popular")
    Call<MovieResponse> getPopularTvShow(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("search/movie/")
    Call<MovieResponse> getMovieBySearch(
            @Query("query") String q,
            @Query("api_key") String apiKey
    );

    @GET("search/tv/")
    Call<MovieResponse> getTvShowBySearch(
            @Query("query") String q,
            @Query("api_key") String apiKey
    );

    @GET("discover/{type}")
    Call<MovieResponse> getMovies(
            @Path("type") String movieType
    );

    @GET("discover/movie")
    Call<MovieResponse> getReleaseMovie(
            @Query("primary_release_date.gte") String date,
            @Query("primary_release_date.lte") String today
    );

}
