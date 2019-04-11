package com.kayali_developer.sobhimohammad.mainactivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kayali_developer.sobhimohammad.AppConstants;
import com.kayali_developer.sobhimohammad.R;
import com.kayali_developer.sobhimohammad.data.model.Video;
import com.kayali_developer.sobhimohammad.data.model.VideoDescriptionResponse;
import com.kayali_developer.sobhimohammad.data.remote.APIService;
import com.kayali_developer.sobhimohammad.data.remote.ApiUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import at.blogc.android.views.ExpandableTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteVideosFragment extends Fragment implements FavoriteVideosAdapter.FavoriteItemsAdapterListener {

    public static final String TAG = "FAVORITE_VIDEOS_FRAGMENT_TAG";

    FavoriteVideosAdapter mAdapter;

    @BindView(R.id.rv_play_list_items)
    RecyclerView rvPlayListItems;
    @BindView(R.id.no_items_view)
    LinearLayout noItemsView;
    private Unbinder unbinder;

    private MainActivity mActivity;

    public FavoriteVideosFragment() {
    }

    private FavoriteVideosFragmentListener mItemsFragmentListener;

    public interface FavoriteVideosFragmentListener {
        void onFavoriteVideoClicked(Video video);
        void onFavoriteVideoLongClicked(Video video);
        void onFavoriteVideosFragmentDetached();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_items, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        mActivity.mViewModel.getFavoritesItemsLive().observe(this, new Observer<List<Video>>() {
            @Override
            public void onChanged(List<Video> videos) {
                populatePlayLists(videos);
            }
        });
        return rootView;
    }

    static FavoriteVideosFragment newInstance() {
        return new FavoriteVideosFragment();
    }

    private void populatePlayLists(List<Video> videos) {
        if (videos != null && videos.size() > 0) {
            mAdapter = new FavoriteVideosAdapter(this);
            RecyclerView.LayoutManager layoutManager;

            int sw = getResources().getConfiguration().smallestScreenWidthDp;
            if (sw >= 600) {
                layoutManager = new GridLayoutManager(getContext(), 2);
            } else {
                layoutManager = new LinearLayoutManager(getContext());
            }

            rvPlayListItems.setLayoutManager(layoutManager);
            rvPlayListItems.hasFixedSize();
            rvPlayListItems.setAdapter(mAdapter);
            mAdapter.setData(videos);
        } else {
            noItemsView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onVideoClicked(Video video) {
        mItemsFragmentListener.onFavoriteVideoClicked(video);
    }

    @Override
    public void onVideoLongClicked(Video video) {
        mItemsFragmentListener.onFavoriteVideoLongClicked(video);
    }

    @Override
    public void onVideoDescriptionExpanded(String videoId, ExpandableTextView tv_exp_description, ImageView iv_expand_description) {
        if (tv_exp_description.isExpanded())
        {
            tv_exp_description.collapse();
            iv_expand_description.setImageResource(R.drawable.ic_keyboard_arrow_down);
        }
        else
        {
            iv_expand_description.setImageResource(R.drawable.ic_timelapse);
            APIService mService = ApiUtils.getAPIService();
            mService.getVideoDescription(
                    MainViewModel.PART_SNIPPET,
                    videoId,
                    MainViewModel.FIELDS_GET_VIDEO_DESCRIPTION,
                    AppConstants.YOUTUBE_DATA_V3_API_KEY
            ).enqueue(new Callback<VideoDescriptionResponse>() {
                @Override
                public void onResponse(Call<VideoDescriptionResponse> call, Response<VideoDescriptionResponse> response) {
                    if (response.body() != null && response.body().getItems() != null && response.body().getItems().size() > 0
                            &&response.body().getItems().get(0).getSnippet() != null ) {
                        tv_exp_description.setText(response.body().getItems().get(0).getSnippet().getDescription());
                        tv_exp_description.expand();
                        iv_expand_description.setImageResource(R.drawable.ic_keyboard_arrow_up);
                    }
                }

                @Override
                public void onFailure(Call<VideoDescriptionResponse> call, Throwable t) {
                    iv_expand_description.setImageResource(R.drawable.ic_keyboard_arrow_down);
                    if (getContext() != null){
                        showToastMessage(getContext().getString(R.string.cannot_load_play_lists));
                    }
                }
            });

        }
    }

    private void showToastMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mItemsFragmentListener = (FavoriteVideosFragmentListener) context;
        mActivity = (MainActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mItemsFragmentListener.onFavoriteVideosFragmentDetached();
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity.mViewModel.updateFavoriteItems();
    }
}