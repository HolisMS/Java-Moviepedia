package satella.app.movies4.activity_detail;


import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import satella.app.movies4.R;
import satella.app.movies4.database.MovieDAO;
import satella.app.movies4.database.MovieDatabase;
import satella.app.movies4.model.Movie;

import static satella.app.movies4.BuildConfig.BASE_URL_BACKDROP;
import static satella.app.movies4.BuildConfig.BASE_URL_POSTER;
import static satella.app.movies4.utilities.Utils.ARG_MOVIE;
import static satella.app.movies4.utilities.Utils.DATABASE_NAME;

public class MovieDetailFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.movie_title)
    TextView mTitle;

    @BindView(R.id.movie_overview)
    TextView mOverview;

    @BindView(R.id.movie_release_date)
    TextView mReleaseDate;

    @BindView(R.id.movie_poster)
    ImageView mPoster;

    @BindView(R.id.movie_user_rating)
    TextView mRatingView;

    @BindView(R.id.main_detail_movie)
    ConstraintLayout constraintLayout;

    @BindView(R.id.progressDetailMovie)
    ProgressBar mProgressBar;

    @BindView(R.id.btn_fav_movie)
    FloatingActionButton btnFav;

    @BindViews({R.id.rating_first_star, R.id.rating_second_star, R.id.rating_third_star, R.id.rating_fourth_star, R.id.rating_fifth_star})
    List<ImageView> mRatingStar;

    private Movie mMovie;

    private MovieDAO movieDAO;

    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_detail, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        movieDAO = Room.databaseBuilder(getContext(), MovieDatabase.class, DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .getMovieDAO();

        inProgress(true);

        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                    Log.d("Error Load", e.getMessage());
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mMovie = getActivity().getIntent().getParcelableExtra(ARG_MOVIE);

                        if (mMovie != null) {
                            inProgress(false);

                            mTitle.setText(mMovie.getOriginalTitle());
                            mOverview.setText(mMovie.getOverview());
                            mReleaseDate.setText(mMovie.getReleaseDate());

                            Activity activity = getActivity();
                            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);

                            if (appBarLayout != null && activity instanceof MovieDetailActivity) {
                                appBarLayout.setTitle(mMovie.getOriginalTitle());
                            }

                            ImageView mBackdrop = activity.findViewById(R.id.movie_backdrop);
                            if (mBackdrop != null) {
                                String backdrop = BASE_URL_BACKDROP + mMovie.getBackdropPath();
                                Glide.with(getActivity())
                                        .load(backdrop)
                                        .apply(new RequestOptions())
                                        .into(mBackdrop);
                            }

                            String poster = BASE_URL_POSTER + mMovie.getPosterPath();
                            Glide.with(getActivity())
                                    .load(poster)
                                    .apply(new RequestOptions())
                                    .into(mPoster);

                            ratingStatus();

                            if (movieDAO.getMovieByTitle(mMovie.getOriginalTitle()) > 0) {
                                btnFav.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_star_fav));
                            }
                        }
                    }
                });
            }
        }).start();

        btnFav.setOnClickListener(this);

    }

    private void ratingStatus() {
        if (mMovie.getVoteAverage() != null && !mMovie.getVoteAverage().isEmpty()) {
            String userRatingStar = getResources().getString(R.string.movie_user_rating,
                    mMovie.getVoteAverage());
            mRatingView.setText(userRatingStar);
            float userRating = Float.valueOf(mMovie.getVoteAverage()) / 2;
            int intPart = (int) userRating;

            for (int i = 0; i < intPart; i++) {
                mRatingStar.get(i).setImageResource(R.drawable.ic_star_black_24dp);
            }

            if (Math.round(userRating) > intPart) {
                mRatingStar.get(intPart).setImageResource(R.drawable.ic_star_half_black_24dp);
            }
        } else {
            mRatingView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void inProgress(boolean b) {
        if (b) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void snackbarInsert() {
        Snackbar snackbar = Snackbar.make(constraintLayout, "success insert favorite", Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();
        TextView tv = snackbarView.findViewById(R.id.snackbar_text);
        tv.setTextColor(Color.BLACK);
        snackbarView.setBackgroundColor(Color.WHITE);
        snackbar.show();
    }

    private void markFavMovie() {
        movieDAO.insert(mMovie);
        getActivity().setResult(Activity.RESULT_OK);
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        getActivity().sendBroadcast(intent);
        btnFav.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_star_fav));
    }

    @Override
    public void onClick(View v) {

        if (v == btnFav) {
            markFavMovie();
            snackbarInsert();

        }
    }


}
