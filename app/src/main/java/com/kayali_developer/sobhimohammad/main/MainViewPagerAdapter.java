package com.kayali_developer.sobhimohammad.main;

import android.content.Context;
import android.view.ViewGroup;

import com.kayali_developer.sobhimohammad.R;

import java.util.HashMap;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MainViewPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private ItemsFragment mItemsFragment;
    private PlayListsFragment mPlayListsFragment;
    private FavoritesVideosFragment mFavoritesVideosFragment;
    public Map<Integer, String> mFragmentTags;
    private FragmentManager fm;

    MainViewPagerAdapter(Context context,
                         FragmentManager fm,
                         ItemsFragment itemsFragment,
                         PlayListsFragment playListsFragment,
                         FavoritesVideosFragment favoritesVideosFragment) {
        super(fm);
        this.fm = fm;
        mFragmentTags = new HashMap<>();
        mContext = context;
        mItemsFragment = itemsFragment;
        mPlayListsFragment = playListsFragment;
        mFavoritesVideosFragment = favoritesVideosFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {

            case 0:
                return mContext.getString(R.string.title_fragment_new_videos);

            case 1:
                return mContext.getString(R.string.title_fragment_play_lists);

            case 2:
                return mContext.getString(R.string.title_fragment_favorites_videos);

            default:
                return mContext.getString(R.string.title_fragment_new_videos);
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object object = super.instantiateItem(container, position);
        if (object instanceof ItemsFragment) {
            ItemsFragment itemsFragment = (ItemsFragment) object;
            String tag = itemsFragment.getTag();
            mFragmentTags.put(position, tag);
        }
        if (object instanceof PlayListsFragment) {
            PlayListsFragment playListsFragment = (PlayListsFragment) object;
            String tag = playListsFragment.getTag();
            mFragmentTags.put(position, tag);
        }
        if (object instanceof FavoritesVideosFragment) {
            FavoritesVideosFragment favoritesVideosFragment = (FavoritesVideosFragment) object;
            String tag = favoritesVideosFragment.getTag();
            mFragmentTags.put(position, tag);
        }
        return object;
    }

    public Fragment getFragment(int position) {
        Fragment fragment = null;
        String tag = mFragmentTags.get(position);
        if (tag != null) {
            fragment = fm.findFragmentByTag(tag);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0:
                return mItemsFragment;

            case 1:
                return mPlayListsFragment;

            case 2:
                return mFavoritesVideosFragment;

            default:
                return mItemsFragment;
        }
    }
}