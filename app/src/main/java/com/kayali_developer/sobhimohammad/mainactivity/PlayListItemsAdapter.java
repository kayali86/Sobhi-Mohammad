package com.kayali_developer.sobhimohammad.mainactivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.kayali_developer.sobhimohammad.R;
import com.kayali_developer.sobhimohammad.data.model.PlayListItem;
import com.kayali_developer.sobhimohammad.data.model.Video;
import com.kayali_developer.sobhimohammad.utilities.AppDateUtils;
import com.kayali_developer.sobhimohammad.utilities.CustomItemConverter;
import com.kayali_developer.sobhimohammad.utilities.Prefs;
import com.kayali_developer.sobhimohammad.utilities.ThemeUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import at.blogc.android.views.ExpandableTextView;
import jp.wasabeef.picasso.transformations.CropTransformation;

public class PlayListItemsAdapter extends RecyclerView.Adapter<PlayListItemsAdapter.PlayListItemsViewHolder> {
    private Context mContext;

    private List<PlayListItem> mPlayListItems;

    private ItemsAdapterListener mItemsAdapterListener;

    PlayListItemsAdapter(ItemsAdapterListener mPlayListsAdapterListener) {
        this.mItemsAdapterListener = mPlayListsAdapterListener;
    }

    public interface ItemsAdapterListener {
        void onPlayListItemClicked(Video video);
    }

    @NonNull
    @Override
    public PlayListItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View rootView = inflater.inflate(R.layout.item_video, parent, false);
        return new PlayListItemsViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayListItemsViewHolder holder, int position) {
        PlayListItem playListItem = mPlayListItems.get(position);
        if (playListItem.getSnippet().getThumbnails() != null && playListItem.getSnippet().getThumbnails().getHigh()!= null){
            String url = playListItem.getSnippet().getThumbnails().getHigh().getUrl();
            Picasso.get().
                    load(url).
                    transform(new CropTransformation(450,256, CropTransformation.GravityHorizontal.CENTER, CropTransformation.GravityVertical.CENTER)).
                    placeholder(R.drawable.progress_animation).
                    error(R.drawable.no_image_available).
                    into(holder.iv_thumbnail_playlist_item);
            List<String> viewedItems = null;
            try {
                viewedItems = Prefs.getViewedItemsAsStringArrayListJson(mContext);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (viewedItems != null && viewedItems.size() > 0 && viewedItems.contains(playListItem.getSnippet().getResourceId().getVideoId())){
                holder.iv_viewed_item.setVisibility(View.VISIBLE);
            }else{
                holder.iv_viewed_item.setVisibility(View.GONE);
            }


            if (url != null){
                holder.item_play_list_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemsAdapterListener.onPlayListItemClicked(CustomItemConverter.playListItemToVideo(playListItem));
                    }
                });
            }

            holder.tv_title.setTextColor(ThemeUtils.getThemePrimaryColor(mContext));
            if (playListItem.getSnippet().getTitle() != null){
                holder.tv_title.setText(playListItem.getSnippet().getTitle());
            }


            if (playListItem.getSnippet().getDescription() != null){
                holder.tv_exp_description.setText(playListItem.getSnippet().getDescription());

                // set animation duration via code, but preferable in your layout files by using the animation_duration attribute
                holder.tv_exp_description.setAnimationDuration(750L);

                // set interpolators for both expanding and collapsing animations
                holder.tv_exp_description.setInterpolator(new OvershootInterpolator());

                // or set them separately
                holder.tv_exp_description.setExpandInterpolator(new OvershootInterpolator());
                holder.tv_exp_description.setCollapseInterpolator(new OvershootInterpolator());
                holder.iv_expand_description.setColorFilter(ThemeUtils.getThemePrimaryColor(mContext));
                holder.iv_expand_description.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.tv_exp_description.isExpanded())
                        {
                            holder.tv_exp_description.collapse();
                            holder.iv_expand_description.setImageResource(R.drawable.ic_keyboard_arrow_down);
                        }
                        else
                        {
                            holder.tv_exp_description.expand();
                            holder.iv_expand_description.setImageResource(R.drawable.ic_keyboard_arrow_up);
                        }
                    }
                });
            }


            if (playListItem.getSnippet().getPublishedAt() != null && AppDateUtils.youtubeFormatToDeFormat(playListItem.getSnippet().getPublishedAt()) != null){
                holder.tv_publish_date.setText(AppDateUtils.youtubeFormatToDeFormat(playListItem.getSnippet().getPublishedAt()));
            }

        }
    }

    @Override
    public int getItemCount() {
        if (mPlayListItems == null || mPlayListItems.size() == 0) return 0;
        return mPlayListItems.size();
    }

    public void setData(List<PlayListItem> playListItems){
        mPlayListItems = filterData(playListItems);
        notifyDataSetChanged();
    }
    private List<PlayListItem> filterData(List<PlayListItem> playListItems){
        List<PlayListItem> filteredPlayListItems = new ArrayList<>();
        if (playListItems != null && playListItems.size() > 0){
            for (PlayListItem playListItem : playListItems){
                if (playListItem != null && playListItem.getSnippet() != null && playListItem.getSnippet().getThumbnails() != null){
                    filteredPlayListItems.add(playListItem);
                }
            }
        }
        return filteredPlayListItems;
    }

    class PlayListItemsViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv_thumbnail_playlist_item;
        private ImageView iv_viewed_item;
        private TextView tv_title;
        private ExpandableTextView tv_exp_description;
        private ImageView iv_expand_description;
        private TextView tv_publish_date;
        private ConstraintLayout item_play_list_item;

        PlayListItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_thumbnail_playlist_item = itemView.findViewById(R.id.iv_thumbnail_playlist_item);
            iv_viewed_item = itemView.findViewById(R.id.iv_viewed_item);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_exp_description = itemView.findViewById(R.id.tv_exp_description);
            iv_expand_description = itemView.findViewById(R.id.iv_expand_description);
            tv_publish_date = itemView.findViewById(R.id.tv_publish_date);
            item_play_list_item = itemView.findViewById(R.id.item_play_list_item);
        }
    }
}
