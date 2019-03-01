package com.kayali_developer.sobhimohammad.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.kayali_developer.sobhimohammad.R;
import com.kayali_developer.sobhimohammad.data.model.PlayListItemsResponse;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NewVideosFragment extends Fragment implements ItemsAdapter.ItemsAdapterListener {
    public static final String TAG = "NewVideosFragmentTag";
    static final String NEW_VIDEOS_RESPONSE_KEY = "new_videos_response_key";
    ItemsAdapter mAdapter;

    @BindView(R.id.rv_play_list_items)
    RecyclerView rvPlayListItems;
    @BindView(R.id.no_items_view)
    LinearLayout noItemsView;
    private Unbinder unbinder;

    private NewVideosFragmentListener mNewVideosFragmentListener;

    public interface NewVideosFragmentListener {
        void onPlayListItemClicked(PlayListItemsResponse.Item item);
    }

    public NewVideosFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_items, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        if (getArguments() != null) {
            List<PlayListItemsResponse.Item> items;

            String responseStr = getArguments().getString(NEW_VIDEOS_RESPONSE_KEY);
            if (responseStr != null) {
                TypeToken<List<PlayListItemsResponse.Item>> token = new TypeToken<List<PlayListItemsResponse.Item>>() {
                };
                try {
                    items = new Gson().fromJson(responseStr, token.getType());
                    populatePlayLists(items);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }

        }
        return rootView;
    }

    static ItemsFragment newInstance() {
        return new ItemsFragment();
    }

    void populatePlayLists(List<PlayListItemsResponse.Item> items) {
        if (items != null && items.size() > 0) {
            mAdapter = new ItemsAdapter(this::onPlayListItemClicked);
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
        mNewVideosFragmentListener.onPlayListItemClicked(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mNewVideosFragmentListener = (NewVideosFragmentListener) context;
    }

}
