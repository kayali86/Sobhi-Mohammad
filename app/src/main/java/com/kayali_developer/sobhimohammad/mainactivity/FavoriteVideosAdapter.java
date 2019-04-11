package com.kayali_developer.sobhimohammad.mainactivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.kayali_developer.sobhimohammad.R;
import com.kayali_developer.sobhimohammad.data.model.Video;
import com.kayali_developer.sobhimohammad.utilities.AppDateUtils;
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

public class FavoriteVideosAdapter extends RecyclerView.Adapter<FavoriteVideosAdapter.PlayListItemsViewHolder> {
    private Context mContext;

    private List<Video> mVideos;

    private FavoriteItemsAdapterListener mItemsAdapterListener;

    FavoriteVideosAdapter(FavoriteItemsAdapterListener mFavoriteItemsAdapterListener) {
        this.mItemsAdapterListener = mFavoriteItemsAdapterListener;
    }

    public interface FavoriteItemsAdapterListener {
        void onVideoClicked(Video video);
        void onVideoLongClicked(Video video);
        void onVideoDescriptionExpanded(String videoId, ExpandableTextView tv_exp_description, ImageView iv_expand_description);
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
        Video video = mVideos.get(position);
        if (video.getSnippet().getThumbnails() != null && video.getSnippet().getThumbnails().getHigh()!= null){
            String url = video.getSnippet().getThumbnails().getHigh().getUrl();
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
            if (viewedItems != null && viewedItems.size() > 0 && viewedItems.contains(video.getId())){
                holder.iv_viewed_item.setVisibility(View.VISIBLE);
            }else{
                holder.iv_viewed_item.setVisibility(View.GONE);
            }


            if (url != null){
                holder.item_play_list_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemsAdapterListener.onVideoClicked(video);
                    }
                });
            }

            if (video.isSelected()){
                holder.item_play_list_item.setBackgroundColor(mContext.getResources().getColor(R.color.lightTextColor));
            }else{
                holder.item_play_list_item.setBackgroundColor(mContext.getResources().getColor(R.color.backgroundColor));
            }

            holder.item_play_list_item.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mItemsAdapterListener.onVideoLongClicked(video);
                    return true;
                }
            });
            holder.tv_title.setTextColor(ThemeUtils.getThemePrimaryColor(mContext));

            if (video.getSnippet().getTitle() != null){
                holder.tv_title.setText(video.getSnippet().getTitle());
            }

            if (video.getSnippet().getDescription() != null){
                holder.tv_exp_description.setText(video.getSnippet().getDescription());

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
                        mItemsAdapterListener.onVideoDescriptionExpanded(video.getId().getVideoId(), holder.tv_exp_description, holder.iv_expand_description);
                    }
                });
            }


            if (video.getSnippet().getPublishedAt() != null && AppDateUtils.youtubeFormatToDeFormat(video.getSnippet().getPublishedAt()) != null){
                holder.tv_publish_date.setText(AppDateUtils.youtubeFormatToDeFormat(video.getSnippet().getPublishedAt()));
            }

        }
    }

    @Override
    public int getItemCount() {
        if (mVideos == null || mVideos.size() == 0) return 0;
        return mVideos.size();
    }

    public void setData(List<Video> videos){
        mVideos = filterData(videos);
        notifyDataSetChanged();
    }

    List<Video> getAllItems(){
        return mVideos;
    }

    private List<Video> filterData(List<Video> videos){
        List<Video> filteredVideos = new ArrayList<>();
        if (videos != null && videos.size() > 0){
            for (Video video : videos){
                if (video != null && video.getSnippet() != null && video.getSnippet().getThumbnails() != null){
                    filteredVideos.add(video);
                }
            }
        }
        return filteredVideos;
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
