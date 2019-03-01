package com.kayali_developer.sobhimohammad.main;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kayali_developer.sobhimohammad.R;
import com.kayali_developer.sobhimohammad.data.model.PlayListsResponse;
import com.kayali_developer.sobhimohammad.utilities.Prefs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainFragment extends Fragment {
    public static final String TAG = "MainFragmentTag";
    public static final String TAB_INDEX_KEY = "tab_index_key";
    public static final int NEW_VIDEOS_TAB_INDEX = 0;
    public static final int PLAY_LISTS_TAB_INDEX = 1;
    public static final int FAVORITE_VIDEOS_TAB_INDEX = 2;

    int mCurrentTab;

    @BindView(R.id.iv_show_new_videos)
    ImageView ivShowNewVideos;
    @BindView(R.id.iv_show_play_lists)
    ImageView ivShowPlayLists;
    @BindView(R.id.iv_show_favorites_videos)
    ImageView ivShowFavoritesVideos;
    @BindView(R.id.new_videos_line)
    View newVideosLine;
    @BindView(R.id.play_lists_line)
    View playListsLine;
    @BindView(R.id.favorite_videos_line)
    View favoriteVideosLine;
    @BindView(R.id.tv_show_new_videos)
    TextView tvShowNewVideos;
    @BindView(R.id.tv_show_play_lists)
    TextView tvShowPlayLists;
    @BindView(R.id.tv_show_favorites_videos)
    TextView tvShowFavoritesVideos;
    private MainActivity mActivity;

    FavoriteVideosFragment mFavoriteVideosFragment;
    PlayListsFragment mPlayListsFragment;
    NewVideosFragment mNewVideosFragment;

    public interface MainFragmentListener {
        void onNewVideosTabClicked();

        void onPlayListsTabClicked();

        void onFavoritesVideosTabClicked();
    }

    private MainFragmentListener mMainFragmentListener;

    public MainFragment() {
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        checkTheme();
        if (getArguments() != null) {
            switch (getArguments().getInt(TAB_INDEX_KEY)) {

                case MainFragment.NEW_VIDEOS_TAB_INDEX:
                    showNewVideosFragment();
                    break;

                case MainFragment.PLAY_LISTS_TAB_INDEX:
                    showPlayListsFragment();
                    break;

                case MainFragment.FAVORITE_VIDEOS_TAB_INDEX:
                    showFavoritesVideosFragment();
                    break;
            }
        } else if (savedInstanceState != null) {
            int index = savedInstanceState.getInt(TAB_INDEX_KEY);
            if (index != -1) {
                switch (index) {

                    case MainFragment.NEW_VIDEOS_TAB_INDEX:
                        showNewVideosFragment();
                        break;

                    case MainFragment.PLAY_LISTS_TAB_INDEX:
                        showPlayListsFragment();
                        break;

                    case MainFragment.FAVORITE_VIDEOS_TAB_INDEX:
                        showFavoritesVideosFragment();
                        break;
                }
            }
        } else {
            showNewVideosFragment();
        }
        return rootView;
    }

    private void checkTheme() {
        Themes currentTheme = Prefs.getCurrentTheme(getActivity());
        int color = getResources().getColor(R.color.colorPrimary);
        switch (currentTheme){

            case PURPLE:
                color = getResources().getColor(R.color.colorPrimary);
                break;

            case BLUE:
                color = getResources().getColor(R.color.blueColorPrimary);
                break;

            case GREEN:
                color = getResources().getColor(R.color.greenColorPrimary);
                break;

            case BROWN:
                color = getResources().getColor(R.color.brownColorPrimary);
                break;

            case YELLOW:
                color = getResources().getColor(R.color.yellowColorPrimary);
                break;

            case RED:
                color = getResources().getColor(R.color.redColorPrimary);
                break;

        }
        ivShowNewVideos.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        ivShowPlayLists.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        ivShowFavoritesVideos.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        newVideosLine.setBackgroundColor(color);
        playListsLine.setBackgroundColor(color);
        favoriteVideosLine.setBackgroundColor(color);
        tvShowFavoritesVideos.setTextColor(color);
        tvShowNewVideos.setTextColor(color);
        tvShowPlayLists.setTextColor(color);
    }

    void showNewVideosFragment() {
        mCurrentTab = NEW_VIDEOS_TAB_INDEX;
        removeAllFragmentsInMainFragment();
        initializeNewVideosFragment();
        mActivity.mFragmentManager.beginTransaction().replace(R.id.main_fragment_fragment_container, mNewVideosFragment, ItemsFragment.NEW_VIDEOS_FRAGMENT_TAG).commitAllowingStateLoss();
        newVideosLine.setVisibility(View.VISIBLE);
        playListsLine.setVisibility(View.GONE);
        favoriteVideosLine.setVisibility(View.GONE);
    }

    void showPlayListsFragment() {
        mCurrentTab = PLAY_LISTS_TAB_INDEX;
        removeAllFragmentsInMainFragment();
        initializePlayListsFragment();
        mActivity.mFragmentManager.beginTransaction().replace(R.id.main_fragment_fragment_container, mPlayListsFragment, PlayListsFragment.TAG).commitAllowingStateLoss();
        newVideosLine.setVisibility(View.GONE);
        playListsLine.setVisibility(View.VISIBLE);
        favoriteVideosLine.setVisibility(View.GONE);
    }

    void showFavoritesVideosFragment() {
        mCurrentTab = FAVORITE_VIDEOS_TAB_INDEX;
        removeAllFragmentsInMainFragment();
        initializeFavoritesVideosFragment();
        mActivity.mFragmentManager.beginTransaction().replace(R.id.main_fragment_fragment_container, mFavoriteVideosFragment, FavoriteVideosFragment.TAG).commitAllowingStateLoss();
        newVideosLine.setVisibility(View.GONE);
        playListsLine.setVisibility(View.GONE);
        favoriteVideosLine.setVisibility(View.VISIBLE);
    }


    void initializeNewVideosFragment() {
        mNewVideosFragment = new NewVideosFragment();
        if (mActivity.mViewModel.getNewVideos() != null && mActivity.mViewModel.getNewVideos().size() > 0) {
            Bundle bundle = new Bundle();
            bundle.putString(NewVideosFragment.NEW_VIDEOS_RESPONSE_KEY, new Gson().toJson(mActivity.mViewModel.getNewVideos()));
            mNewVideosFragment.setArguments(bundle);
        }
    }


    void initializePlayListsFragment() {
        mPlayListsFragment = PlayListsFragment.newInstance();
        if (mActivity.mViewModel.getAllPlayLists() != null && mActivity.mViewModel.getAllPlayLists().size() > 0) {
            PlayListsResponse playListsResponse = new PlayListsResponse();
            playListsResponse.setItems(mActivity.mViewModel.getAllPlayLists());
            Bundle bundle = new Bundle();
            bundle.putString(PlayListsFragment.PLAY_LISTS_KEY, new Gson().toJson(playListsResponse));
            mPlayListsFragment.setArguments(bundle);
        }
    }

    void initializeFavoritesVideosFragment() {
        mFavoriteVideosFragment = FavoriteVideosFragment.newInstance();
    }

    private void findAllFragmentsInMainFragment() {
        if (mNewVideosFragment == null) {
            mNewVideosFragment = (NewVideosFragment) mActivity.mFragmentManager.findFragmentByTag(NewVideosFragment.TAG);
        }
        if (mPlayListsFragment == null) {
            mPlayListsFragment = (PlayListsFragment) mActivity.mFragmentManager.findFragmentByTag(PlayListsFragment.TAG);
        }
        if (mFavoriteVideosFragment == null) {
            mFavoriteVideosFragment = (FavoriteVideosFragment) mActivity.mFragmentManager.findFragmentByTag(FavoriteVideosFragment.TAG);
        }
    }

    private void removeAllFragmentsInMainFragment() {
        findAllFragmentsInMainFragment();
        removeNewVideosFragment();
        removePlayListsFragment();
        removeFavoritesVideosFragment();
    }

    private void removeNewVideosFragment() {
        if (mNewVideosFragment == null) {
            findAllFragmentsInMainFragment();
        } else {
            mActivity.mFragmentManager.beginTransaction().remove(mNewVideosFragment).commitAllowingStateLoss();
            mNewVideosFragment = null;
        }
    }

    private void removePlayListsFragment() {
        if (mPlayListsFragment == null) {
            findAllFragmentsInMainFragment();
        } else {
            mActivity.mFragmentManager.beginTransaction().remove(mPlayListsFragment).commitAllowingStateLoss();
            mPlayListsFragment = null;
        }
    }

    private void removeFavoritesVideosFragment() {
        if (mFavoriteVideosFragment == null) {
            findAllFragmentsInMainFragment();
        } else {
            mActivity.mFragmentManager.beginTransaction().remove(mFavoriteVideosFragment).commitAllowingStateLoss();
            mFavoriteVideosFragment = null;
        }
    }

    @OnClick({R.id.show_new_videos_layout, R.id.show_play_lists_layout, R.id.show_favorites_videos_layout})
    void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.show_new_videos_layout:
                mMainFragmentListener.onNewVideosTabClicked();
                break;

            case R.id.show_play_lists_layout:
                mMainFragmentListener.onPlayListsTabClicked();
                break;

            case R.id.show_favorites_videos_layout:
                mMainFragmentListener.onFavoritesVideosTabClicked();
                break;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(TAB_INDEX_KEY, mCurrentTab);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = ((MainActivity) context);
        mMainFragmentListener = ((MainFragmentListener) context);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
