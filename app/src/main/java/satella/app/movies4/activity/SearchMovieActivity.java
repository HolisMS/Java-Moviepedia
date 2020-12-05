package satella.app.movies4.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import satella.app.movies4.R;
import satella.app.movies4.activity_detail.MovieDetailActivity;
import satella.app.movies4.adapter.MovieAdapter;
import satella.app.movies4.model.ApiInterface;
import satella.app.movies4.model.Movie;
import satella.app.movies4.model.MovieResponse;
import satella.app.movies4.utilities.MovieClient;

import static satella.app.movies4.utilities.Utils.API_KEY;
import static satella.app.movies4.utilities.Utils.ARG_MOVIE;
import static satella.app.movies4.utilities.Utils.INTENT_SEARCH;
import static satella.app.movies4.utilities.Utils.INTENT_TAG;

public class SearchMovieActivity extends AppCompatActivity {

    @BindView(R.id.rv_search_movie)
    RecyclerView mRecyclerView;

    @BindView(R.id.toolbar_search_movie)
    Toolbar mToolbar;

    @BindView(R.id.progres_search_movie)
    ProgressBar mProgressBar;

    private MovieAdapter mAdapter;
    private ArrayList<Movie> mMoviesList;
    private ApiInterface mServices;
    private Call<MovieResponse> mMovieCall;

    private boolean tabletView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        tabletView = findViewById(R.id.movie_detail_container) != null;

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.search_movie));
        }

        mMoviesList = new ArrayList<>();



        if (getIntent() != null) {
            if (getIntent().getStringExtra(INTENT_TAG).equals("search")) {
                String q = getIntent().getStringExtra(INTENT_SEARCH);
                initView();
                getMovies(q);
            }
        }

    }

    private void initView() {
        mAdapter = new MovieAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void getMovies(final String q) {
        mServices = MovieClient.getClient().create(ApiInterface.class);
        mMovieCall = mServices.getMovieBySearch(q, API_KEY);

        inProgress(true);

        mMovieCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if (response.body() != null) {
                    mMoviesList = response.body().getMovies();
                    Objects.requireNonNull(getSupportActionBar()).setSubtitle(getString(R.string.search_hint_result,
                            Objects.requireNonNull(response.body()).getTotalResults().toString(), q));

                }

                mAdapter.setMovieResult(mMoviesList);
                mRecyclerView.setAdapter(mAdapter);

                inProgress(false);

                mAdapter.setOnItemClickCallkBack(new MovieAdapter.OnItemClickCallBack() {
                    @Override
                    public void send_details(Movie movie) {
                        if (!tabletView) {
                            Intent intent = new Intent(SearchMovieActivity.this, MovieDetailActivity.class);
                            intent.putExtra(ARG_MOVIE, movie);
                            startActivity(intent);
                        }
                    }
                });
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                Toast.makeText(SearchMovieActivity.this, "Something went wrong"
                        , Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void inProgress(boolean b) {
        if (b) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
