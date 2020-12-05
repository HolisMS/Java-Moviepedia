package satella.app.favoritemovie4;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteHolder> {
    private Cursor mCursor;
    private Context mContext;

    public FavoriteAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public FavoriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false);
        return new FavoriteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteHolder holder, int position) {
        holder.bind(mCursor.moveToPosition(position));
    }

    @Override
    public int getItemCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    public void setDataFav(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }

    class FavoriteHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_backdrop)
        ImageView mBackdrop;
        @BindView(R.id.img_poster)
        ImageView mPoster;
        @BindView(R.id.tv_title)
        TextView mTitle;
        @BindView(R.id.tv_vote)
        TextView mVote;

        FavoriteHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(boolean position) {
            if (position) {
                mTitle.setText(mCursor.getString(mCursor.getColumnIndexOrThrow(Utils.COLUMN_TITLE)));
                mVote.setText(mCursor.getString(mCursor.getColumnIndexOrThrow(Utils.COLUMN_VOTE)));

                Glide.with(mContext)
                        .load(Utils.BASE_URL_BACKDROP + mCursor.getString(mCursor.getColumnIndexOrThrow(Utils.COLUMN_BACKDROP)))
                        .into(mBackdrop);

                Glide.with(mContext)
                        .load(Utils.BASE_URL_POSTER + mCursor.getString(mCursor.getColumnIndexOrThrow(Utils.COLUMN_POSTER)))
                        .into(mPoster);
            }
        }
    }
}
