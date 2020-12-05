package satella.app.movies4.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import satella.app.movies4.R;
import satella.app.movies4.model.Movie;

import static satella.app.movies4.BuildConfig.BASE_URL_BACKDROP;
import static satella.app.movies4.BuildConfig.BASE_URL_POSTER;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {

    private ArrayList<Movie> mList = new ArrayList<>();
    private Context mContext;

    public MovieAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setmList(ArrayList<Movie> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void setMovieResult(ArrayList<Movie> movieResult) {
        this.mList = movieResult;
    }

    private OnItemClickCallBack onItemClickCallBack;

    public void setOnItemClickCallkBack(OnItemClickCallBack onItemClickCallkBack){
        this.onItemClickCallBack = onItemClickCallkBack;
    }

    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
        holder.bind(mList.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallBack.send_details(mList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MovieHolder extends RecyclerView.ViewHolder {
        private ImageView mThumbnail, mThumbnail2;
        private TextView mTitle, mVote;

        MovieHolder(@NonNull View itemView) {
            super(itemView);
            mThumbnail = itemView.findViewById(R.id.img_thumbnail);
            mThumbnail2 = itemView.findViewById(R.id.img_thumbnail2);
            mTitle = itemView.findViewById(R.id.tv_title);
            mVote = itemView.findViewById(R.id.tv_vote);
        }

        void bind(Movie movies) {
            mTitle.setText(movies.getOriginalTitle());
            mVote.setText(movies.getVoteAverage());

            String poster = BASE_URL_POSTER + movies.getPosterPath();
            Glide.with(itemView)
                    .load(poster)
                    .apply(new RequestOptions())
                    .into(mThumbnail2);

            String backdrop = BASE_URL_BACKDROP + movies.getBackdropPath();
            Glide.with(itemView)
                    .load(backdrop)
                    .apply(RequestOptions.placeholderOf(R.drawable.load))
                    .into(mThumbnail);
        }
    }

    public interface OnItemClickCallBack {
        void send_details(Movie movies);

    }
}
