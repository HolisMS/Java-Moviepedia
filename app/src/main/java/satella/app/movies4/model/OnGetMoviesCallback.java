package satella.app.movies4.model;

import java.util.ArrayList;

public interface OnGetMoviesCallback {
    void onSuccess(ArrayList<Movie> movies);

    void onError();

}
