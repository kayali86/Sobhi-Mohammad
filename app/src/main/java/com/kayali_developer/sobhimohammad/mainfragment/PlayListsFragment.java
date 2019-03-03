package com.kayali_developer.sobhimohammad.mainfragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.kayali_developer.sobhimohammad.R;
import com.kayali_developer.sobhimohammad.data.model.PlayListsResponse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PlayListsFragment extends Fragment implements PlayListsAdapter.PlayListsAdapterListener {
    public static final String TAG = "PlayListsFragmentTag";
    static final String PLAY_LISTS_KEY = "play_list_key";

    @BindView(R.id.rv_play_lists)
    RecyclerView rvPlayLists;
    private Unbinder unbinder;
    private PlayListsAdapter mAdapter;
    private PlayListsFragmentListener mPlayListsFragmentListener;

    public interface PlayListsFragmentListener {
        void onPlayListItemClicked(String playListId);
    }

    public PlayListsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_play_lists, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        populatePlayLists();

        if (getArguments() != null) {
            if (getArguments().getString(PLAY_LISTS_KEY) != null) {
                try {
                    PlayListsResponse playListsResponse = new Gson().fromJson(getArguments().getString(PLAY_LISTS_KEY), PlayListsResponse.class);
                    mAdapter.setData(playListsResponse.getItems());
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }
        }

        return rootView;
    }

    static PlayListsFragment newInstance() {
        return new PlayListsFragment();
    }

    private void populatePlayLists() {
        mAdapter = new PlayListsAdapter(this::onPlayListClicked, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvPlayLists.setLayoutManager(layoutManager);
        rvPlayLists.hasFixedSize();
        rvPlayLists.setAdapter(mAdapter);
    }

    @Override
    public void onPlayListClicked(String playListId) {
        mPlayListsFragmentListener.onPlayListItemClicked(playListId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mPlayListsFragmentListener = (PlayListsFragmentListener) context;
    }
}
