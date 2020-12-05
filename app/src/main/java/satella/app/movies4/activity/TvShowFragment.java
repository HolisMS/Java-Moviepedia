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

public class TvShowFragment extends Fragment {

    @BindView(R.id.search_tv)
    SearchView mSearchTv;

    @BindView(R.id.rv_tvshow)
    RecyclerView mRecyclerView;

    @BindView(R.id.progressBarTvShow)
    ProgressBar mProgressBar;

    private MovieAdapter mAdapter;
    private MovieRepository movieRepository;

    private boolean tabletView;

    public TvShowFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tv_show, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

        tabletView = view.findViewById(R.id.movie_detail_container) != null;

        showTvRecycler();
    }

    private void showTvRecycler() {
        mAdapter = new MovieAdapter(getActivity());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        inProgress(true);

        movieRepository = ViewModelProviders.of(this).get(MovieRepository.class);
        movieRepository.setMovies("tv");
        movieRepository.getDataMovies().observe(this, getMovies);

    }

    @Override
    public void onStart() {
        super.onStart();
        if (mSearchTv != null) {
            mSearchTv.setQueryHint(Html.fromHtml("<font color = #000000>" +
                    getString(R.string.search_hint) + "</font>"));
            mSearchTv.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Intent intent = new Intent(getContext(), SearchTvShowActivity.class);
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
