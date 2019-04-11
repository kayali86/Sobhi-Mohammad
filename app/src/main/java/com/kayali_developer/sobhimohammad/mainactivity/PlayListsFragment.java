package com.kayali_developer.sobhimohammad.mainactivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kayali_developer.sobhimohammad.R;
import com.kayali_developer.sobhimohammad.data.model.PlayList;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PlayListsFragment extends Fragment implements PlayListsAdapter.PlayListsAdapterListener {
    public static final String TAG = "PlayListsFragmentTag";

    @BindView(R.id.rv_play_lists)
    RecyclerView rvPlayLists;
    private Unbinder unbinder;
    private PlayListsFragmentListener mPlayListsFragmentListener;

    private MainActivity mActivity;

    public interface PlayListsFragmentListener {
        void onPlayListClicked(String playListId);
    }

    public PlayListsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_play_lists, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        populatePlayLists(mActivity.mViewModel.getAllPlayLists());
    }

    static PlayListsFragment newInstance() {
        return new PlayListsFragment();
    }

    void populatePlayLists(List<PlayList> playLists) {
        PlayListsAdapter mAdapter = new PlayListsAdapter(this::onPlayListClicked, getContext());
        RecyclerView.LayoutManager layoutManager;

        int sw = getResources().getConfiguration().smallestScreenWidthDp;
        if (sw >= 600) {
            layoutManager = new GridLayoutManager(getContext(), 2);
        } else {
            layoutManager = new LinearLayoutManager(getContext());
        }
        rvPlayLists.setLayoutManager(layoutManager);
        rvPlayLists.hasFixedSize();
        rvPlayLists.setAdapter(mAdapter);
        mAdapter.setData(playLists);
    }

    @Override
    public void onPlayListClicked(String playListId) {
        mPlayListsFragmentListener.onPlayListClicked(playListId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = ((MainActivity) context);
        mPlayListsFragmentListener = (PlayListsFragmentListener) context;
    }
}
