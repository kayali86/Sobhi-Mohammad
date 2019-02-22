package com.kayali_developer.sobhimohammad.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kayali_developer.sobhimohammad.R;
import com.kayali_developer.sobhimohammad.data.model.PlayListsResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import jp.wasabeef.picasso.transformations.CropTransformation;

public class PlayListsAdapter extends RecyclerView.Adapter<PlayListsAdapter.PlayListItemsViewHolder> {
    private List<PlayListsResponse.Item> mItems;

    private PlayListsAdapterListener mPlayListsAdapterListener;

    PlayListsAdapter(PlayListsAdapterListener mPlayListsAdapterListener) {
        this.mPlayListsAdapterListener = mPlayListsAdapterListener;
    }

    interface PlayListsAdapterListener{
        void onPlayListClicked(String PlayListId);
    }

    @NonNull
    @Override
    public PlayListItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View rootView = inflater.inflate(R.layout.item_play_list, parent, false);
        return new PlayListItemsViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayListItemsViewHolder holder, int position) {
        PlayListsResponse.Item item = mItems.get(position);
        Picasso.get().
                load(item.getSnippet().getThumbnails().getHigh().getUrl()).
                transform(new CropTransformation(990,500, CropTransformation.GravityHorizontal.CENTER, CropTransformation.GravityVertical.CENTER)).
                fit().centerCrop().
                placeholder(R.drawable.progress_animation).
                error(R.drawable.no_image_available).
                into(holder.iv_thumbnail_playlist);

        if (item.getSnippet().getTitle() != null){
            holder.tv_play_list_title.setText(item.getSnippet().getTitle());
        }

        holder.item_play_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayListsAdapterListener.onPlayListClicked(item.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mItems == null || mItems .size() == 0) return 0;
        return mItems.size();
    }

    void setData(List<PlayListsResponse.Item> items){
        mItems =items;
        notifyDataSetChanged();
    }

    class PlayListItemsViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv_thumbnail_playlist;
        private TextView tv_play_list_title;
        private LinearLayout item_play_list;

        PlayListItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_thumbnail_playlist = itemView.findViewById(R.id.iv_thumbnail_playlist);
            tv_play_list_title = itemView.findViewById(R.id.tv_play_list_title);
            item_play_list = itemView.findViewById(R.id.item_play_list);
        }
    }
}
