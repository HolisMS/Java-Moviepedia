package satella.app.movies4.widgets;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import satella.app.movies4.BuildConfig;
import satella.app.movies4.R;
import satella.app.movies4.database.MovieDAO;
import satella.app.movies4.database.MovieDatabase;
import satella.app.movies4.model.Movie;

import static satella.app.movies4.utilities.Utils.DATABASE_NAME;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final Context mContext;
    private final ArrayList<Movie> mMoviesItem = new ArrayList<>();
    private MovieDatabase database;


    StackRemoteViewsFactory(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {
        final long identity = Binder.clearCallingIdentity();
        database = Room.databaseBuilder(mContext.getApplicationContext(), MovieDatabase.class, DATABASE_NAME)
                .allowMainThreadQueries()
                .build();
        Binder.restoreCallingIdentity(identity);

    }

    @Override
    public void onDataSetChanged() {
        try {
            MovieDAO movieDAO = database.getMovieDAO();
            mMoviesItem.clear();
            mMoviesItem.addAll(movieDAO.getAllFavMovies());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        database.close();
    }

    @Override
    public int getCount() {
        return mMoviesItem.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.item_widget);
        try {
            Bitmap bitmap = Glide.with(mContext)
                    .asBitmap()
                    .load(BuildConfig.BASE_URL_POSTER + mMoviesItem.get(position).getPosterPath())
                    .apply(new RequestOptions().fitCenter())
                    .submit(700,400)
                    .get();

            remoteViews.setImageViewBitmap(R.id.imageView, bitmap);

        } catch (InterruptedException | ExecutionException e) {
            Log.d("Widget load error : ", "error");
        }


        Bundle bundle = new Bundle();
        bundle.putInt(BannerWidget.EXTRA_ITEM, position);
        Intent intent = new Intent();
        intent.putExtras(bundle);

        remoteViews.setOnClickFillInIntent(R.id.imageView, intent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
