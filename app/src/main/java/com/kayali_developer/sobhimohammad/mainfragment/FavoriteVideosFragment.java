package com.kayali_developer.sobhimohammad.mainfragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kayali_developer.sobhimohammad.R;
import com.kayali_developer.sobhimohammad.data.model.PlayListItemsResponse;
import com.kayali_developer.sobhimohammad.mainactivity.MainActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FavoriteVideosFragment extends Fragment implements FavoriteItemsAdapter.FavoriteItemsAdapterListener {

    public static final String TAG = "FAVORITE_VIDEOS_FRAGMENT_TAG";

    public FavoriteItemsAdapter mAdapter;

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
        void onPlayListItemClicked(PlayListItemsResponse.Item item);
        void onPlayListItemLongClicked(PlayListItemsResponse.Item item);
        void onFavoriteVideosFragmentDetached();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_items, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        mActivity.mViewModel.getFavoritesItemsLive().observe(this, new Observer<List<PlayListItemsResponse.Item>>() {
            @Override
            public void onChanged(List<PlayListItemsResponse.Item> items) {
                populatePlayLists(items);
            }
        });
        return rootView;
    }

    static FavoriteVideosFragment newInstance() {
        return new FavoriteVideosFragment();
    }

    private void populatePlayLists(List<PlayListItemsResponse.Item> items) {
        if (items != null && items.size() > 0) {
            mAdapter = new FavoriteItemsAdapter(this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            rvPlayListItems.setLayoutManager(layoutManager);
            rvPlayListItems.hasFixedSize();
            rvPlayListItems.setAdapter(mAdapter);
            mAdapter.setData(items);
        } else {
            noItemsView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPlayListItemClicked(PlayListItemsResponse.Item item) {
        mItemsFragmentListener.onPlayListItemClicked(item);
    }

    @Override
    public void onPlayListItemLongClicked(PlayListItemsResponse.Item item) {
        mItemsFragmentListener.onPlayListItemLongClicked(item);
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
