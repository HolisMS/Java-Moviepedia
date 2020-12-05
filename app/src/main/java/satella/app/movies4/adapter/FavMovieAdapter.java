package satella.app.movies4.adapter;

import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import satella.app.movies4.R;
import satella.app.movies4.database.MovieDAO;
import satella.app.movies4.database.MovieDatabase;
import satella.app.movies4.model.Movie;

import static satella.app.movies4.BuildConfig.BASE_URL_BACKDROP;
import static satella.app.movies4.BuildConfig.BASE_URL_POSTER;
import static satella.app.movies4.utilities.Utils.DATABASE_NAME;

public class FavMovieAdapter extends RecyclerView.Adapter<FavMovieAdapter.FavoriteHolder> {

    private ArrayList<Movie> mList = new ArrayList<>();
    private Context mContext;

    public FavMovieAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setmList(ArrayList<Movie> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public ArrayList<Movie> getmList() {
        return mList;
    }

    @NonNull
    @Override
    public FavoriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false);
        return new FavoriteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteHolder holder, int position) {
        holder.bind(mList.get(position));


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class FavoriteHolder extends RecyclerView.ViewHolder {
        private ImageView mBackdrop, mposter;
        private TextView mTitle, mVote;
        private Button btnRemove;

        FavoriteHolder(@NonNull View itemView) {
            super(itemView);
            mBackdrop = itemView.findViewById(R.id.img_backdrop_fav);
            mposter = itemView.findViewById(R.id.img_poster_fav);
            mTitle = itemView.findViewById(R.id.tv_title_fav);
            mVote = itemView.findViewById(R.id.tv_vote_fav);
            btnRemove = itemView.findViewById(R.id.btn_remove_fav);
        }

        void bind(Movie movies) {
            mTitle.setText(movies.getOriginalTitle());
            mVote.setText(movies.getVoteAverage());

            String poster = BASE_URL_POSTER + movies.getPosterPath();
            Glide.with(itemView)
                    .load(poster)
                    .apply(new RequestOptions())
                    .into(mposter);

            String backdrop = BASE_URL_BACKDROP + movies.getBackdropPath();
            Glide.with(itemView)
                    .load(backdrop)
                    .apply(RequestOptions.placeholderOf(R.drawable.load))
                    .into(mBackdrop);

            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                    builder.setMessage(R.string.massage_delete);
                    builder.setCancelable(false)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    MovieDAO movieDAO = Room.databaseBuilder(itemView.getContext(), MovieDatabase.class, DATABASE_NAME)
                                            .allowMainThreadQueries()
                                            .build()
                                            .getMovieDAO();
                                    movieDAO.deleteByUid(mList.get(getAdapterPosition()).getUid());
                                    mList.remove(movies);
                                    notifyDataSetChanged();
                                    Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                                    mContext.sendBroadcast(intent);
                                    snackbarDelete(v);
                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    builder.setCancelable(true);
                                }
                            });
                    builder.show();
                }
            });

        }
    }

    private void snackbarDelete(View v) {
        Snackbar snackbar = Snackbar.make(v, R.string.delete_success, Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();
        TextView tv = snackbarView.findViewById(R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snackbarView.setBackgroundColor(Color.BLACK);
        snackbar.show();
    }

}
