package com.kayali_developer.sobhimohammad.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.kayali_developer.sobhimohammad.AppConstants;
import com.kayali_developer.sobhimohammad.R;
import com.kayali_developer.sobhimohammad.data.model.PlayListItemsResponse;
import com.kayali_developer.sobhimohammad.data.model.VideoStatisticsResponse;
import com.kayali_developer.sobhimohammad.utilities.MyTextUtils;
import com.kayali_developer.sobhimohammad.utilities.Prefs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener {
    public static final String ITEM_KEY = "item_key";
    public static final String VIDEO_STATISTICS_KEY = "video_statistics_key";
    private static final int RECOVERY_DIALOG_REQUEST = 1;

    @BindView(R.id.tv_view_count)
    TextView tvViewCount;
    @BindView(R.id.tv_likes)
    TextView tvLikes;
    @BindView(R.id.tv_dislikes)
    TextView tvDislikes;
    @BindView(R.id.tv_item_description)
    TextView tvItemDescription;
    @BindView(R.id.tv_item_title)
    TextView tvItemTitle;
    @BindView(R.id.iv_favorite)
    ImageView ivFavorite;
    @BindView(R.id.line2)
    View line2;
    @BindView(R.id.iv_view_count)
    ImageView ivViewCount;
    @BindView(R.id.iv_likes)
    ImageView ivLikes;
    @BindView(R.id.iv_dislikes)
    ImageView ivDislikes;
    @BindView(R.id.favorite)
    LinearLayout favorite;
    @BindView(R.id.iv_share)
    ImageView ivShare;
    @BindView(R.id.share)
    LinearLayout share;
    @BindView(R.id.line1)
    View line1;
    @BindView(R.id.video_activity_layout)
    LinearLayout videoActivityLayout;


    private VideoViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);
        mViewModel = ViewModelProviders.of(this).get(VideoViewModel.class);
        checkIntent();
        checkDarkMode();
        YouTubePlayerFragment youTubePlayerFragment =
                (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_video_fragment);
        youTubePlayerFragment.initialize(AppConstants.YOUTUBE_DATA_V3_API_KEY, this);
    }

    private void checkIntent() {
        if (getIntent() != null) {
            if (getIntent().getStringExtra(ITEM_KEY) != null) {
                try {
                    mViewModel.setCurrentItem(new Gson().fromJson(getIntent().getStringExtra(ITEM_KEY), PlayListItemsResponse.Item.class));
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }


                if (mViewModel.getCurrentItem() != null && mViewModel.getCurrentItem().getSnippet() != null) {
                    if (mViewModel.getCurrentItem().getSnippet().getTitle() != null) {
                        tvItemTitle.setText(mViewModel.getCurrentItem().getSnippet().getTitle());
                        setTitle(mViewModel.getCurrentItem().getSnippet().getTitle());
                    }
                    if (mViewModel.getCurrentItem().getSnippet().getDescription() != null) {
                        tvItemDescription.setText(mViewModel.getCurrentItem().getSnippet().getDescription());
                    }
                    Prefs.addToViewedItems(this, mViewModel.getCurrentItem().getId());
                }
            }

            if (getIntent().getStringExtra(VIDEO_STATISTICS_KEY) != null) {
                try {
                    mViewModel.setItemStatistics(new Gson().fromJson(getIntent().getStringExtra(VIDEO_STATISTICS_KEY), VideoStatisticsResponse.class));
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }

                if (mViewModel.getItemStatistics() != null) {
                    if (mViewModel.getItemStatistics().getItems().get(0).getStatistics().getViewCount() != null) {
                        String viewCount = mViewModel.getItemStatistics().getItems().get(0).getStatistics().getViewCount();
                        tvViewCount.setText(MyTextUtils.formatViewsCount(viewCount));
                    }
                    if (mViewModel.getItemStatistics().getItems().get(0).getStatistics().getLikeCount() != null) {
                        String likeCount = mViewModel.getItemStatistics().getItems().get(0).getStatistics().getLikeCount();
                        tvLikes.setText(MyTextUtils.formatViewsCount(likeCount));
                    }
                    if (mViewModel.getItemStatistics().getItems().get(0).getStatistics().getDislikeCount() != null) {
                        String dislikeCount = mViewModel.getItemStatistics().getItems().get(0).getStatistics().getDislikeCount();
                        tvDislikes.setText(MyTextUtils.formatViewsCount(dislikeCount));
                    }
                }
            }

            if (mViewModel.getCurrentItem() != null) {

                if (mViewModel.isFavorite(mViewModel.getCurrentItem().getId())) {
                    ivFavorite.setImageResource(R.drawable.ic_heart);
                } else {
                    ivFavorite.setImageResource(R.drawable.ic_heart_outline);
                }

            }

        }
    }


    private void checkDarkMode() {
        if (Prefs.getDarkModeStatus(this)) {
            videoActivityLayout.setBackgroundColor(getResources().getColor(R.color.darkBackgroundColor));
            tvViewCount.setTextColor(getResources().getColor(R.color.lightTextColor));
            tvLikes.setTextColor(getResources().getColor(R.color.lightTextColor));
            tvDislikes.setTextColor(getResources().getColor(R.color.lightTextColor));
            tvItemDescription.setTextColor(getResources().getColor(R.color.lightTextColor));
            tvItemTitle.setTextColor(getResources().getColor(R.color.lightTextColor));
            ivFavorite.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
            line2.setBackgroundColor(getResources().getColor(R.color.lightTextColor));
            ivViewCount.setColorFilter(ContextCompat.getColor(this, R.color.lightTextColor), android.graphics.PorterDuff.Mode.SRC_IN);
            ivLikes.setColorFilter(ContextCompat.getColor(this, R.color.lightTextColor), android.graphics.PorterDuff.Mode.SRC_IN);
            ivDislikes.setColorFilter(ContextCompat.getColor(this, R.color.lightTextColor), android.graphics.PorterDuff.Mode.SRC_IN);
            ivShare.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
            line1.setBackgroundColor(getResources().getColor(R.color.lightTextColor));
        }
    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            youTubePlayer.cueVideo(mViewModel.getCurrentItem().getSnippet().getResourceId().getVideoId());
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format("There was an error initializing the YouTubePlayer (%1$s)", youTubeInitializationResult.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    private void removeAddToFavorites() {
        if (mViewModel.isFavorite(mViewModel.getCurrentItem().getId())) {
            ivFavorite.setImageResource(R.drawable.ic_heart_outline);
            boolean success = mViewModel.removeFromFavorites(mViewModel.getCurrentItem().getId());
            if (success) {
                showToastMessage("Removed");
            } else {
                showToastMessage("Error");
            }
        } else {
            ivFavorite.setImageResource(R.drawable.ic_heart);
            boolean success = mViewModel.addToFavorites(mViewModel.getCurrentItem());
            if (success) {
                showToastMessage("Added");
            } else {
                showToastMessage("Error");
            }
        }
    }


    private void shareVideo() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "https://www.youtube.com/watch?v=" + mViewModel.getCurrentItem().getSnippet().getResourceId().getVideoId();
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    private void showToastMessage(String message) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.favorite, R.id.share})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.favorite:
                removeAddToFavorites();
                break;
            case R.id.share:
                shareVideo();
                break;
        }
    }
}
