package com.kayali_developer.sobhimohammad.mainactivity;


import android.content.Context;

import com.kayali_developer.sobhimohammad.R;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MainViewPagerAdapter extends FragmentPagerAdapter {
    static final int NEW_VIDEOS_FRAGMENT_INDEX = 0;
    static final int PLAY_LISTS_FRAGMENT_INDEX = 1;
    static final int FAVORITE_VIDEOS_FRAGMENT_INDEX = 2;

    private Context mContext;

    private NewVideosFragment mNewVideosFragment;
    private PlayListsFragment mPlayListsFragment;
    private FavoriteVideosFragment mFavoriteVideosFragment;

    MainViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;

        String newVideosFragmentTag = makeFragmentTag(R.id.main_view_pager, NEW_VIDEOS_FRAGMENT_INDEX);
        String playListsFragmentTag = makeFragmentTag(R.id.main_view_pager, PLAY_LISTS_FRAGMENT_INDEX);
        String favoriteVideosFragmentTag = makeFragmentTag(R.id.main_view_pager, FAVORITE_VIDEOS_FRAGMENT_INDEX);

        for (Fragment fragment : fm.getFragments()){
            if (fragment != null && fragment.getTag() != null){
                if (fragment.getTag().equals(newVideosFragmentTag)){
                    mNewVideosFragment = (NewVideosFragment) fm.findFragmentByTag(makeFragmentTag(R.id.main_view_pager, NEW_VIDEOS_FRAGMENT_INDEX));
                }else if (fragment.getTag().equals(playListsFragmentTag)){
                    mPlayListsFragment = (PlayListsFragment) fm.findFragmentByTag(makeFragmentTag(R.id.main_view_pager, PLAY_LISTS_FRAGMENT_INDEX));

                } else if (fragment.getTag().equals(favoriteVideosFragmentTag)){
                    mFavoriteVideosFragment = (FavoriteVideosFragment) fm.findFragmentByTag(makeFragmentTag(R.id.main_view_pager, FAVORITE_VIDEOS_FRAGMENT_INDEX));

                }
            }
        }
    }

    private static String makeFragmentTag(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {

            case NEW_VIDEOS_FRAGMENT_INDEX:
                return mContext.getString(R.string.title_fragment_new_videos);

            case PLAY_LISTS_FRAGMENT_INDEX:
                return mContext.getString(R.string.title_fragment_play_lists);

            case FAVORITE_VIDEOS_FRAGMENT_INDEX:
                return mContext.getString(R.string.title_fragment_favorites_videos);

            default:
                return mContext.getString(R.string.title_fragment_new_videos);
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case NEW_VIDEOS_FRAGMENT_INDEX:
                if (mNewVideosFragment == null) mNewVideosFragment = new NewVideosFragment();

                return mNewVideosFragment;

            case PLAY_LISTS_FRAGMENT_INDEX:
                if (mPlayListsFragment == null) mPlayListsFragment = new PlayListsFragment();
                return mPlayListsFragment;

            case FAVORITE_VIDEOS_FRAGMENT_INDEX:
                if (mFavoriteVideosFragment == null) mFavoriteVideosFragment = new FavoriteVideosFragment();
                return mFavoriteVideosFragment;

            default:
                if (mNewVideosFragment == null) mNewVideosFragment = new NewVideosFragment();
                return mNewVideosFragment;
        }
    }

    @Override
    public int getItemPosition(Object object) {
        // POSITION_NONE makes it possible to reload the PagerAdapter
        return POSITION_NONE;
    }

    NewVideosFragment getNewVideosFragment() {
        return mNewVideosFragment;
    }

    PlayListsFragment getPlayListsFragment() {
        return mPlayListsFragment;
    }

    FavoriteVideosFragment getFavoriteVideosFragment() {
        return mFavoriteVideosFragment;
    }

}
