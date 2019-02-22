package com.kayali_developer.sobhimohammad.main;

import android.animation.Animator;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.kayali_developer.sobhimohammad.R;
import com.kayali_developer.sobhimohammad.data.model.PlayListItemsResponse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FavoritesVideosFragment extends Fragment implements ItemsAdapter.ItemsAdapterListener {
    public static final String TAG = "FavoritesVideosFragmentTag";
    public static final String PLAY_LIST_FAVORITES_ITEMS_RESPONSE_KEY = "play_list_favorites_items_response_key";

    @BindView(R.id.rv_play_list_items)
    RecyclerView rvPlayListItems;
    @BindView(R.id.favorites_fragment_layout)
    LinearLayout favoritesFragmentLayout;
    private Unbinder unbinder;

    private FavoritesVideosFragmentListener mFavoritesVideosFragmentListener;

    public interface FavoritesVideosFragmentListener {
        void onPlayListItemClicked(PlayListItemsResponse.Item item);
    }

    public FavoritesVideosFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorites_videos, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        if (getArguments() != null) {
            String responseStr = getArguments().getString(PLAY_LIST_FAVORITES_ITEMS_RESPONSE_KEY);
            if (responseStr != null) {
                try {
                    PlayListItemsResponse response = new Gson().fromJson(responseStr, PlayListItemsResponse.class);
                    populatePlayLists(response);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }

        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        onAnimationStart();
    }

    private void onAnimationStart() {
        if (Build.VERSION.SDK_INT >= 21){
            // get the center for the clipping circle
            int cx = (favoritesFragmentLayout.getRight());
            int cy = (favoritesFragmentLayout.getBottom());

            // get the final radius for the clipping circle
            int finalRadius = Math.max(favoritesFragmentLayout.getWidth(), favoritesFragmentLayout.getHeight());

            // create the animator for this view (the start radius is zero)
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(favoritesFragmentLayout, cx, cy, 0, finalRadius);
            anim.setDuration(1000);

            // make the view visible and start the animation
            favoritesFragmentLayout.setVisibility(View.VISIBLE);
            anim.start();
        }
    }

    public static FavoritesVideosFragment newInstance() {
        return new FavoritesVideosFragment();
    }

    public void populatePlayLists(PlayListItemsResponse response) {
        if (response != null) {
            ItemsAdapter adapter = new ItemsAdapter(this::onPlayListItemClicked);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            rvPlayListItems.setLayoutManager(layoutManager);
            rvPlayListItems.hasFixedSize();
            rvPlayListItems.setAdapter(adapter);
            adapter.setData(response.getItems());
        }
    }

    @Override
    public void onPlayListItemClicked(PlayListItemsResponse.Item item) {
        mFavoritesVideosFragmentListener.onPlayListItemClicked(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mFavoritesVideosFragmentListener = (FavoritesVideosFragmentListener) context;
    }
}
