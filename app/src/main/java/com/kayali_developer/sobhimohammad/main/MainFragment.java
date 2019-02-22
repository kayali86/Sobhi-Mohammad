package com.kayali_developer.sobhimohammad.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.kayali_developer.sobhimohammad.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

public class MainFragment extends Fragment {
    public static final String TAG = "MainFragmentTag";
    @BindView(R.id.main_tab_layout)
    TabLayout mainTabLayout;
    @BindView(R.id.main_view_pager)
    ViewPager mainViewPager;
    private MainActivity mActivity;

    private MainViewPagerAdapter adapter;

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
        initializeViewPager(mActivity.mItemsFragment, mActivity.mPlayListsFragment, mActivity.mFavoritesVideosFragment);

        return rootView;
    }

    private void initializeViewPager(ItemsFragment itemsFragment,
                                     PlayListsFragment playListsFragment,
                                     FavoritesVideosFragment favoritesVideosFragment) {

        adapter = new MainViewPagerAdapter(getContext(), mActivity.mFragmentManager, itemsFragment, playListsFragment, favoritesVideosFragment);
        Timber.e(">>>>>>>>>>>>>>>>>>>>>>>>>>>>" + adapter.mFragmentTags.size());
        mainViewPager.setAdapter(adapter);
        // Give the TabLayout the ViewPager
        mainTabLayout.setupWithViewPager(mainViewPager);
        setTabsIcons(mainTabLayout);
    }

    private void setTabsIcons(TabLayout tabLayout) {
        try {
            tabLayout.getTabAt(0).setIcon(R.drawable.ic_new_icon_purple);
            tabLayout.getTabAt(1).setIcon(R.drawable.ic_playlist_purple);
            tabLayout.getTabAt(2).setIcon(R.drawable.ic_favorites_videos_list_purple);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = ((MainActivity) context);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
