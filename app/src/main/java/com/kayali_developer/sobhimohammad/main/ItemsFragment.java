package com.kayali_developer.sobhimohammad.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import timber.log.Timber;

public class ItemsFragment extends Fragment implements ItemsAdapter.ItemsAdapterListener {
    public static final String TAG = "PlayListItemsFragmentTag";
    public static final String PLAY_LIST_ITEMS_RESPONSE_KEY = "play_list_items_response_key";

    private MainActivity mActivity;

    @BindView(R.id.rv_play_list_items)
    RecyclerView rvPlayListItems;
    private Unbinder unbinder;

    public ItemsFragment() {
    }

    private ItemsFragmentListener mItemsFragmentListener;
    public interface ItemsFragmentListener {
        void onPlayListItemClicked(PlayListItemsResponse.Item item);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_items, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        if (getArguments() != null){
            String responseStr = getArguments().getString(PLAY_LIST_ITEMS_RESPONSE_KEY);
            if (responseStr != null){
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
    public static ItemsFragment newInstance() {
        return new ItemsFragment();
    }
    private void populatePlayLists(PlayListItemsResponse response) {
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
        Timber.e(">>>>>>>>>>>>>>>>>>>>>>>onPlayListItemClicked " + item);
        mItemsFragmentListener.onPlayListItemClicked(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mItemsFragmentListener = (ItemsFragmentListener) context;
        mActivity = ((MainActivity) context);
    }
}
