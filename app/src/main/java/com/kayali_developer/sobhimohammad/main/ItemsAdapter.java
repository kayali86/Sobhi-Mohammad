package com.kayali_developer.sobhimohammad.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.kayali_developer.sobhimohammad.R;
import com.kayali_developer.sobhimohammad.data.model.PlayListItemsResponse;
import com.kayali_developer.sobhimohammad.utilities.AppDateUtils;
import com.kayali_developer.sobhimohammad.utilities.Prefs;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import at.blogc.android.views.ExpandableTextView;
import jp.wasabeef.picasso.transformations.CropTransformation;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.PlayListItemsViewHolder> {
    private Context mContext;

    private List<PlayListItemsResponse.Item> mItems;

    private ItemsAdapterListener mItemsAdapterListener;

    public ItemsAdapter(ItemsAdapterListener mPlayListsAdapterListener) {
        this.mItemsAdapterListener = mPlayListsAdapterListener;
    }

    public interface ItemsAdapterListener {
        void onPlayListItemClicked(PlayListItemsResponse.Item item);
    }

    @NonNull
    @Override
    public PlayListItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View rootView = inflater.inflate(R.layout.item_play_list_item, parent, false);
        return new PlayListItemsViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayListItemsViewHolder holder, int position) {
        PlayListItemsResponse.Item item = mItems.get(position);
        if (item.getSnippet().getThumbnails() != null && item.getSnippet().getThumbnails().getHigh()!= null){
            String url = item.getSnippet().getThumbnails().getHigh().getUrl();
            Picasso.get().
                    load(url).
                    transform(new CropTransformation(990,500, CropTransformation.GravityHorizontal.CENTER, CropTransformation.GravityVertical.CENTER)).
                    fit().centerCrop().
                    placeholder(R.drawable.progress_animation).
                    error(R.drawable.no_image_available).
                    into(holder.iv_thumbnail_playlist_item);
            List<String> viewedItems = null;
            try {
                viewedItems = Prefs.getViewedItemsAsStringArrayListJson(mContext);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (viewedItems != null && viewedItems.size() > 0 && viewedItems.contains(item.getId())){
                holder.iv_viewed_item.setVisibility(View.VISIBLE);
            }else{
                holder.iv_viewed_item.setVisibility(View.GONE);
            }


            if (url != null){
                holder.iv_thumbnail_playlist_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemsAdapterListener.onPlayListItemClicked(item);
                    }
                });
            }

            if (item.getSnippet().getTitle() != null){
                holder.tv_title.setText(item.getSnippet().getTitle());
            }

            if (item.getSnippet().getDescription() != null){
                holder.tv_exp_description.setText(item.getSnippet().getDescription());

                // set animation duration via code, but preferable in your layout files by using the animation_duration attribute
                holder.tv_exp_description.setAnimationDuration(750L);

                // set interpolators for both expanding and collapsing animations
                holder.tv_exp_description.setInterpolator(new OvershootInterpolator());

                // or set them separately
                holder.tv_exp_description.setExpandInterpolator(new OvershootInterpolator());
                holder.tv_exp_description.setCollapseInterpolator(new OvershootInterpolator());
                holder.tv_expand_description.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.tv_exp_description.isExpanded())
                        {
                            holder.tv_exp_description.collapse();
                            holder.tv_expand_description.setImageResource(R.drawable.ic_keyboard_arrow_down);
                        }
                        else
                        {
                            holder.tv_exp_description.expand();
                            holder.tv_expand_description.setImageResource(R.drawable.ic_keyboard_arrow_up);
                        }
                    }
                });

            }


            if (item.getSnippet().getPublishedAt() != null && AppDateUtils.youtubeFormatToDeFormat(item.getSnippet().getPublishedAt()) != null){
                holder.tv_publish_date.setText(AppDateUtils.youtubeFormatToDeFormat(item.getSnippet().getPublishedAt()));
            }

        }
    }

    @Override
    public int getItemCount() {
        if (mItems == null || mItems .size() == 0) return 0;
        return mItems.size();
    }

    public void setData(List<PlayListItemsResponse.Item> items){
        mItems = filterData(items);
        notifyDataSetChanged();
    }
    private List<PlayListItemsResponse.Item> filterData(List<PlayListItemsResponse.Item> items){
        List<PlayListItemsResponse.Item> filteredItems = new ArrayList<>();
        if (items != null && items.size() > 0){
            for (PlayListItemsResponse.Item item : items){
                if (item != null && item.getSnippet() != null && item.getSnippet().getThumbnails() != null){
                    filteredItems.add(item);
                }
            }
        }
        return filteredItems;
    }

    class PlayListItemsViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv_thumbnail_playlist_item;
        private ImageView iv_viewed_item;
        private TextView tv_title;
        private ExpandableTextView tv_exp_description;
        private ImageView tv_expand_description;
        private TextView tv_publish_date;

        PlayListItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_thumbnail_playlist_item = itemView.findViewById(R.id.iv_thumbnail_playlist_item);
            iv_viewed_item = itemView.findViewById(R.id.iv_viewed_item);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_exp_description = itemView.findViewById(R.id.tv_exp_description);
            tv_expand_description = itemView.findViewById(R.id.tv_expand_description);
            tv_publish_date = itemView.findViewById(R.id.tv_publish_date);
        }
    }
}
