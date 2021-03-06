package satella.app.movies4.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import satella.app.movies4.R;
import satella.app.movies4.activity_detail.MovieDetailActivity;
import satella.app.movies4.adapter.MovieAdapter;
import satella.app.movies4.model.Movie;
import satella.app.movies4.utilities.MovieRepository;

import static satella.app.movies4.utilities.Utils.ARG_MOVIE;
import static satella.app.movies4.utilities.Utils.INTENT_SEARCH;
import static satella.app.movies4.utilities.Utils.INTENT_TAG;

public class MovieFragment extends Fragment {

    @BindView(R.id.search_movie)
    SearchView mSearchMovie;

    @BindView(R.id.rv_movies)
    RecyclerView mRecyclerView;

    @BindView(R.id.progressBarMovie)
    ProgressBar mProgressBar;

    private MovieAdapter mAdapter;

    private boolean tabletView;

    public MovieFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

        tabletView = view.findViewById(R.id.movie_detail_container) != null;

        showMovieRecycler();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mSearchMovie != null) {
            mSearchMovie.setQueryHint(Html.fromHtml("<font color = #000000>" +
                    getString(R.string.search_hint) + "</font>"));
            mSearchMovie.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Intent intent = new Intent(getContext(), SearchMovieActivity.class);
                    intent.putExtra(INTENT_SEARCH, query);
                    intent.putExtra(INTENT_TAG,"search");
                    startActivity(intent);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
    }

    private void showMovieRecycler() {
        mAdapter = new MovieAdapter(getActivity());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        inProgress(true);

        MovieRepository movieRepository = ViewModelProviders.of(this).get(MovieRepository.class);
        movieRepository.setMovies("movie");
        movieRepository.getDataMovies().observe(this, getMovies);

    }

    private Observer<ArrayList<Movie>> getMovies = new Observer<ArrayList<Movie>>() {
        @Override
        public void onChanged(ArrayList<Movie> movies) {
            if (movies != null) {
                inProgress(false);
                mAdapter.setmList(movies);

                mAdapter.setOnItemClickCallkBack(new MovieAdapter.OnItemClickCallBack() {
                    @Override
                    public void send_details(Movie movies) {
                        if (!tabletView) {
                            Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                            intent.putExtra(ARG_MOVIE, movies);
                            startActivity(intent);
                        }
                    }
                });
            }
        }
    };

    private void inProgress(boolean b){
        if (b){
            mProgressBar.setVisibility(View.VISIBLE);
        }else {
            mProgressBar.setVisibility(View.GONE);
        }

    }



}
