package satella.app.movies4.utilities;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import satella.app.movies4.activity.MainActivity;
import satella.app.movies4.model.ApiInterface;
import satella.app.movies4.model.Movie;
import satella.app.movies4.model.MovieResponse;

public class MovieRepository extends ViewModel {
    private MutableLiveData<ArrayList<Movie>> movieList = new MutableLiveData<>();
    private ApiInterface apiService = MovieClient.getClient().create(ApiInterface.class);

    public void setMovies(final String type) {
        Call<MovieResponse> call = apiService.getMovies(type);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, Response<MovieResponse> response) {
                try {
                    ArrayList<Movie> movies = response.body().getMovies();
                    for (Movie data : movies) {
                        data.setMovieType(type);
                    }
                    movieList.postValue(movies);
                }catch (Exception e) {
                    Log.d(MainActivity.class.getSimpleName(), e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, Throwable t) {
                Log.d(MainActivity.class.getSimpleName(), t.getLocalizedMessage());
            }
        });
    }

    public void setFavMovie(ArrayList<Movie> movies) {
        movieList.postValue(movies);
    }

    public LiveData<ArrayList<Movie>> getDataMovies() {
        return movieList;
    }

}
