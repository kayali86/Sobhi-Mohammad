package com.kayali_developer.sobhimohammad.videoactivity;

import android.content.Intent;
import android.graphics.PorterDuff;
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
import com.kayali_developer.sobhimohammad.data.model.Video;
import com.kayali_developer.sobhimohammad.utilities.MyTextUtils;
import com.kayali_developer.sobhimohammad.utilities.Prefs;
import com.kayali_developer.sobhimohammad.utilities.ThemeUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener {

    public static final String ITEM_KEY = "item_key";
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
    @BindView(R.id.iv_share)
    ImageView ivShare;
    @BindView(R.id.line1)
    View line1;
    @BindView(R.id.video_activity_layout)
    LinearLayout videoActivityLayout;
    @BindView(R.id.tv_favorite)
    TextView tvFavorite;
    @BindView(R.id.tv_share)
    TextView tvShare;


    private VideoViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);
        mViewModel = ViewModelProviders.of(this).get(VideoViewModel.class);
        tvItemTitle.setTextColor(ThemeUtils.getThemePrimaryColor(this));
        tvFavorite.setTextColor(ThemeUtils.getThemePrimaryColor(this));
        tvShare.setTextColor(ThemeUtils.getThemePrimaryColor(this));
        ivFavorite.setColorFilter(ThemeUtils.getThemePrimaryColor(this));
        ivShare.setColorFilter(ThemeUtils.getThemePrimaryColor(this));
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
                    mViewModel.setCurrentItem(new Gson().fromJson(getIntent().getStringExtra(ITEM_KEY), Video.class));
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
                    Prefs.addToViewedItems(this, mViewModel.getCurrentItem().getId().getVideoId());

                    if (mViewModel.getCurrentItem().getStatistics() != null) {
                        if (mViewModel.getCurrentItem().getStatistics().getViewCount() != null) {
                            String viewCount = mViewModel.getCurrentItem().getStatistics().getViewCount();
                            tvViewCount.setText(MyTextUtils.formatViewsCount(viewCount));
                        }
                        if (mViewModel.getCurrentItem().getStatistics().getLikeCount() != null) {
                            String likeCount = mViewModel.getCurrentItem().getStatistics().getLikeCount();
                            tvLikes.setText(MyTextUtils.formatViewsCount(likeCount));
                        }
                        if (mViewModel.getCurrentItem().getStatistics().getDislikeCount() != null) {
                            String dislikeCount = mViewModel.getCurrentItem().getStatistics().getDislikeCount();
                            tvDislikes.setText(MyTextUtils.formatViewsCount(dislikeCount));
                        }
                    }

                }
            }

            if (mViewModel.getCurrentItem() != null) {

                if (mViewModel.isFavorite(mViewModel.getCurrentItem().getId().getVideoId())) {
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
            ivFavorite.setColorFilter(ThemeUtils.getThemePrimaryColor(this), PorterDuff.Mode.SRC_IN);
            line2.setBackgroundColor(getResources().getColor(R.color.lightTextColor));
            ivViewCount.setColorFilter(ContextCompat.getColor(this, R.color.lightTextColor), PorterDuff.Mode.SRC_IN);
            ivLikes.setColorFilter(ContextCompat.getColor(this, R.color.lightTextColor), PorterDuff.Mode.SRC_IN);
            ivDislikes.setColorFilter(ContextCompat.getColor(this, R.color.lightTextColor), PorterDuff.Mode.SRC_IN);
            ivShare.setColorFilter(ThemeUtils.getThemePrimaryColor(this), PorterDuff.Mode.SRC_IN);
            line1.setBackgroundColor(getResources().getColor(R.color.lightTextColor));
        }
    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            youTubePlayer.cueVideo(mViewModel.getCurrentItem().getId().getVideoId());
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
        if (mViewModel.isFavorite(mViewModel.getCurrentItem().getId().getVideoId())) {
            ivFavorite.setImageResource(R.drawable.ic_heart_outline);
            boolean success = mViewModel.removeFromFavorites(mViewModel.getCurrentItem().getId().getVideoId());
            if (success) {
                showToastMessage(getString(R.string.removed_from_favorites));
            } else {
                showToastMessage(getString(R.string.removing_error));
            }
        } else {
            ivFavorite.setImageResource(R.drawable.ic_heart);
            boolean success = mViewModel.addToFavorites(mViewModel.getCurrentItem());
            if (success) {
                showToastMessage(getString(R.string.added_to_favorites));
            } else {
                showToastMessage(getString(R.string.adding_error));
            }
        }
    }


    private void shareVideo() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "https://www.youtube.com/watch?v=" + mViewModel.getCurrentItem().getId().getVideoId();
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_via)));
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
