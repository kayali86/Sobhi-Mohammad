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

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoViewHolder> {
    private Context mContext;

    private List<Video> mVideos;

    private VideosAdapterListener mVideosAdapterListener;

    VideosAdapter(VideosAdapterListener videosAdapterListener) {
        this.mVideosAdapterListener = videosAdapterListener;
    }

    public interface VideosAdapterListener {
        void onVideoClicked(Video video);
        void onVideoDescriptionExpanded(String videoId, ExpandableTextView tv_exp_description, ImageView iv_expand_description);
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View rootView = inflater.inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
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
            if (viewedItems != null && viewedItems.size() > 0 && viewedItems.contains(video.getId().getVideoId())){
                holder.iv_viewed_item.setVisibility(View.VISIBLE);
            }else{
                holder.iv_viewed_item.setVisibility(View.GONE);
            }


            if (url != null){
                holder.item_play_list_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mVideosAdapterListener.onVideoClicked(video);
                    }
                });
            }

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
                        mVideosAdapterListener.onVideoDescriptionExpanded(video.getId().getVideoId(), holder.tv_exp_description, holder.iv_expand_description);
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

    public void setData(List<Video> playListItems){
        mVideos = filterData(playListItems);
        notifyDataSetChanged();
    }
    private List<Video> filterData(List<Video> playListItems){
        List<Video> filteredPlayListItems = new ArrayList<>();
        if (playListItems != null && playListItems.size() > 0){
            for (Video playListItem : playListItems){
                if (playListItem != null && playListItem.getSnippet() != null && playListItem.getSnippet().getThumbnails() != null){
                    filteredPlayListItems.add(playListItem);
                }
            }
        }
        return filteredPlayListItems;
    }

    class VideoViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv_thumbnail_playlist_item;
        private ImageView iv_viewed_item;
        private TextView tv_title;
        private ExpandableTextView tv_exp_description;
        private ImageView iv_expand_description;
        private TextView tv_publish_date;
        private ConstraintLayout item_play_list_item;

        VideoViewHolder(@NonNull View itemView) {
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
