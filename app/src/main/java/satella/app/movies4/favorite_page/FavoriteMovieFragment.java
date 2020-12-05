package satella.app.movies4.favorite_page;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import satella.app.movies4.R;
import satella.app.movies4.adapter.FavMovieAdapter;
import satella.app.movies4.database.MovieDAO;
import satella.app.movies4.database.MovieDatabase;
import satella.app.movies4.model.Movie;
import satella.app.movies4.utilities.MovieRepository;

import static satella.app.movies4.utilities.Utils.ARG_MOVIE_FAV_STATE;
import static satella.app.movies4.utilities.Utils.DATABASE_NAME;

public class FavoriteMovieFragment extends Fragment {

    private FavMovieAdapter mAdapter;
    private boolean tabletView;

    @BindView(R.id.rv_fav_movie)
    RecyclerView mRecyclerView;

    @BindView(R.id.progresFavMovie)
    ProgressBar mProgressBar;

    private MovieRepository movieRepository;


    public FavoriteMovieFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        Objects.requireNonNull(getActivity()).setTitle(getString(R.string.favorite_movie));

        tabletView = view.findViewById(R.id.movie_detail_container) != null;

        initView();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(ARG_MOVIE_FAV_STATE, mAdapter.getmList());
    }

    private void initView() {
        mAdapter = new FavMovieAdapter(getActivity());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        inProgress(true);

        ArrayList<Movie> data = (ArrayList<Movie>) loadFavorite();

        movieRepository = ViewModelProviders.of(this).get(MovieRepository.class);
        movieRepository.setFavMovie(data);
        movieRepository.getDataMovies().observe(this, getMovies);

    }

    private List<Movie> loadFavorite() {
        MovieDatabase database = Room.databaseBuilder(getActivity(), MovieDatabase.class, DATABASE_NAME)
                .allowMainThreadQueries()
                .build();
        MovieDAO movieDAO = database.getMovieDAO();
        return movieDAO.getMoviesByMovieType("movie");
    }

    private Observer<ArrayList<Movie>> getMovies = new Observer<ArrayList<Movie>>() {
        @Override
        public void onChanged(ArrayList<Movie> movies) {
            if (movies != null) {
                inProgress(false);
                mAdapter.setmList(movies);
            }
        }
    };

    private void inProgress(boolean b) {
        if (b) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
    }


}

