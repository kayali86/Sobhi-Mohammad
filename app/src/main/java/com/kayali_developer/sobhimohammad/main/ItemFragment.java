package com.kayali_developer.sobhimohammad.main;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.kayali_developer.sobhimohammad.AppConstants;
import com.kayali_developer.sobhimohammad.R;
import com.kayali_developer.sobhimohammad.data.model.VideoStatisticsResponse;
import com.kayali_developer.sobhimohammad.utilities.MyTextUtils;
import com.kayali_developer.sobhimohammad.utilities.Prefs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ItemFragment extends Fragment implements YouTubePlayer.OnInitializedListener {
    public static final String TAG = "ItemFragmentTag";
    public static final String ITEM_KEY = "item_key";
    public static final String VIDEO_STATISTICS_KEY = "video_statistics_key";

    private static final int RECOVERY_DIALOG_REQUEST = 1;
    @BindView(R.id.iv_favorite)
    ImageView ivFavorite;
    @BindView(R.id.iv_share)
    ImageView ivShare;
    @BindView(R.id.tv_likes)
    TextView tvLikes;
    @BindView(R.id.tv_dislikes)
    TextView tvDislikes;
    @BindView(R.id.tv_item_title)
    TextView tvItemTitle;
    @BindView(R.id.tv_item_description)
    TextView tvItemDescription;
    @BindView(R.id.tv_view_count)
    TextView tvViewCount;
    private MainActivity mActivity;
    private Unbinder unbinder;


    private ItemFragmentListener mItemFragmentListener;

    interface ItemFragmentListener{
        void onRemoveAddToFavoritesClicked();
        void onShareVideoClicked();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        checkArguments();
        YouTubePlayerFragment youTubePlayerFragment = YouTubePlayerFragment.newInstance();
        youTubePlayerFragment.initialize(AppConstants.YOUTUBE_DATA_V3_API_KEY,
                this);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_video_fragment, youTubePlayerFragment).commitAllowingStateLoss();
        return rootView;
    }


    private void checkArguments() {
        if (getArguments() != null) {

            if (getArguments().getString(ITEM_KEY) != null) {
                if (mActivity.mViewModel.getCurrentItem() != null && mActivity.mViewModel.getCurrentItem().getSnippet() != null) {
                    if (mActivity.mViewModel.getCurrentItem().getSnippet().getTitle() != null) {
                        tvItemTitle.setText(mActivity.mViewModel.getCurrentItem().getSnippet().getTitle());
                    }
                    if (mActivity.mViewModel.getCurrentItem().getSnippet().getDescription() != null) {
                        tvItemDescription.setText(mActivity.mViewModel.getCurrentItem().getSnippet().getDescription());
                    }
                    Prefs.addToViewedItems(mActivity, mActivity.mViewModel.getCurrentItem().getId());
                }
            }
            if (getArguments().getString(VIDEO_STATISTICS_KEY) != null) {
                VideoStatisticsResponse videoStatisticsResponse = null;
                try {
                    videoStatisticsResponse = new Gson().fromJson(getArguments().getString(VIDEO_STATISTICS_KEY), VideoStatisticsResponse.class);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                if (videoStatisticsResponse != null) {
                    if (videoStatisticsResponse.getItems().get(0).getStatistics().getViewCount() != null) {
                        String viewCount = videoStatisticsResponse.getItems().get(0).getStatistics().getViewCount();
                        tvViewCount.setText(MyTextUtils.formatViewsCount(viewCount));
                    }
                    if (videoStatisticsResponse.getItems().get(0).getStatistics().getLikeCount() != null) {
                        String likeCount = videoStatisticsResponse.getItems().get(0).getStatistics().getLikeCount();
                        tvLikes.setText(MyTextUtils.formatViewsCount(likeCount));
                    }
                    if (videoStatisticsResponse.getItems().get(0).getStatistics().getDislikeCount() != null) {
                        String dislikeCount = videoStatisticsResponse.getItems().get(0).getStatistics().getDislikeCount();
                        tvDislikes.setText(MyTextUtils.formatViewsCount(dislikeCount));
                    }
                }
            }

            if (mActivity.mViewModel.getCurrentItem() != null){
                if (mActivity.mViewModel.isFavorite(mActivity.mViewModel.getCurrentItem().getId())){
                    ivFavorite.setImageResource(R.drawable.ic_heart);
                }else{
                    ivFavorite.setImageResource(R.drawable.ic_heart_outline);
                }
            }

        }
    }




    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b && mActivity.mViewModel.getCurrentItem().getSnippet().getResourceId().getVideoId() != null) {
            youTubePlayer.cueVideo(mActivity.mViewModel.getCurrentItem().getSnippet().getResourceId().getVideoId());
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(getActivity(), RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format("There was an error initializing the YouTubePlayer (%1$s)", youTubeInitializationResult.toString());
            Toast.makeText(mActivity, errorMessage, Toast.LENGTH_LONG).show();
        }
    }


    @OnClick({R.id.iv_favorite, R.id.iv_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_favorite:
                mItemFragmentListener.onRemoveAddToFavoritesClicked();
                break;

            case R.id.iv_share:
                mItemFragmentListener.onShareVideoClicked();
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mItemFragmentListener = ((ItemFragmentListener) context);
        mActivity = ((MainActivity) context);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
