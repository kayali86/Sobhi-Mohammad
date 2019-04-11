package com.kayali_developer.sobhimohammad.mainactivity;


import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.kayali_developer.sobhimohammad.LoadingFragment;
import com.kayali_developer.sobhimohammad.R;
import com.kayali_developer.sobhimohammad.aboutus.AboutUsActivity;
import com.kayali_developer.sobhimohammad.data.model.PlayList;
import com.kayali_developer.sobhimohammad.data.model.PlayListItem;
import com.kayali_developer.sobhimohammad.data.model.Video;
import com.kayali_developer.sobhimohammad.data.model.VideoStatisticsResponse;
import com.kayali_developer.sobhimohammad.settings.SettingsActivity;
import com.kayali_developer.sobhimohammad.utilities.Prefs;
import com.kayali_developer.sobhimohammad.utilities.ThemeUtils;
import com.kayali_developer.sobhimohammad.videoactivity.VideoActivity;

import java.util.List;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity

        extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        PlayListsFragment.PlayListsFragmentListener,
        PlayListItemsFragment.ItemsFragmentListener,
        FavoriteVideosFragment.FavoriteVideosFragmentListener,
        ChangeThemeFragment.ChangeThemeFragmentListener,
        HomePageFragment.HomePageFragmentListener,
        PrivacyPolicyFragment.PrivacyPolicyFragmentListener,
        NewVideosFragment.NewVideosFragmentListener {

    @BindView(R.id.main_tab_layout)
    TabLayout mainTabLayout;
    @BindView(R.id.main_view_pager)
    ViewPager mainViewPager;
    private Context mContext;
    public MainViewModel mViewModel;
    public FragmentManager mFragmentManager;
    private LoadingFragment mLoadingFragment;
    private HomePageFragment mHomePageFragment;
    private ChangeThemeFragment mChangeThemeFragment;
    private PlayListItemsFragment mPlayListItemsFragment;
    private PrivacyPolicyFragment mPrivacyPolicyFragment;

    public MenuItem backMenuItem;
    MenuItem deleteMenuItem;
    public Toolbar mToolbar;
    public NavigationView mNavigationView;
    public DrawerLayout mDrawer;

    private MainViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mFragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            if (!Prefs.getNewVideoNotificationInitializedStatus(mContext)) {
                FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.new_video_topic))
                        .addOnCompleteListener((this::onSubscribedToNewVideoTopic));
            }

            initializeLoadingFragment(getString(R.string.loading));
        }

        viewPagerAdapter = new MainViewPagerAdapter(mContext, mFragmentManager);
        mainViewPager.setAdapter(viewPagerAdapter);
        mainTabLayout.setupWithViewPager(mainViewPager);
        setTabsStyle();

        findAllFragments();
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        observeVariables();
        if (mainViewPager != null && (mViewModel.getAllPlayLists() == null || mViewModel.getAllPlayLists().size() <= 0)) {
            mainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if (position == MainViewPagerAdapter.PLAY_LISTS_FRAGMENT_INDEX &&
                            (mViewModel.getAllPlayLists() == null || mViewModel.getAllPlayLists().size() <= 0)) {
                        initializeLoadingFragment(getString(R.string.loading));
                        mViewModel.loadAllPlayLists();
                    }
                    if (position == MainViewPagerAdapter.FAVORITE_VIDEOS_FRAGMENT_INDEX){
                        mViewModel.updateFavoriteItems();
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

    }

    private void onSubscribedToNewVideoTopic(Task<Void> task) {
        if (!task.isSuccessful()) {
            showToastMessage(getString(R.string.subscription_error));
        } else {
            Prefs.setNewVideoNotificationInitialized(mContext, true);
            Prefs.setNewVideosNotificationStatus(mContext, true);
        }
    }

    private void setTabsStyle() {
        for (int i = 0; i < mainTabLayout.getTabCount(); i++) {
            try {
                View v = LayoutInflater.from(mContext).inflate(R.layout.custom_tab, null);
                TextView tv = v.findViewById(android.R.id.text1);
                tv.setTextColor(ThemeUtils.getThemePrimaryColor(mContext));
                mainTabLayout.getTabAt(i).setCustomView(v);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        setTabsIcons(mainTabLayout);
    }

    private void setTabsIcons(TabLayout tabLayout) {
        try {
            tabLayout.getTabAt(0).setIcon(ThemeUtils.getAppropriateTabIcon(mContext, R.drawable.ic_new_icon_purple));
            tabLayout.getTabAt(1).setIcon(ThemeUtils.getAppropriateTabIcon(mContext, R.drawable.ic_playlist_purple));
            tabLayout.getTabAt(2).setIcon(ThemeUtils.getAppropriateTabIcon(mContext, R.drawable.ic_heart_purple));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onNewVideosLiveChanged(List<Video> videos) {
        mViewModel.setNewVideos(videos);
        if (mFragmentManager != null && mLoadingFragment != null) {
            mFragmentManager.beginTransaction().remove(mLoadingFragment).commitAllowingStateLoss();
        }

        if (videos != null && videos.size() > 0 && viewPagerAdapter.getNewVideosFragment() != null) {
            viewPagerAdapter.getNewVideosFragment().populateVideos(videos);
            mainViewPager.getAdapter().notifyDataSetChanged();
            setTabsStyle();
        }
    }

    private void onVideosStatisticsResponseChanged(VideoStatisticsResponse videoStatisticsResponse) {
        if (videoStatisticsResponse != null && videoStatisticsResponse.getItems() != null &&
                videoStatisticsResponse.getItems().size() > 0 && videoStatisticsResponse.getItems().get(0) != null &&
                videoStatisticsResponse.getItems().get(0).getStatistics() != null
        ) {
            mFragmentManager.beginTransaction().remove(mLoadingFragment).commitAllowingStateLoss();
            Video currentVideo = mViewModel.getCurrentItem();
            currentVideo.setStatistics(videoStatisticsResponse.getItems().get(0).getStatistics());
            startVideoActivity(currentVideo);
        }
    }

    private void onVideoDescriptionResponseChanged(String description){
        Video video = mViewModel.getCurrentItem();
        Video.Snippet snippet = mViewModel.getCurrentItem().getSnippet();
        snippet.setDescription(description);
        video.setSnippet(snippet);
        mViewModel.setCurrentItem(video);
        mViewModel.requestVideoStatistics(mViewModel.getCurrentItem().getId().getVideoId());
    }

    private void onAllPlayListsLiveChanged(List<PlayList> playLists) {
        mViewModel.setAllPlayLists(playLists);
        if (mFragmentManager != null && mLoadingFragment != null) {
            mFragmentManager.beginTransaction().remove(mLoadingFragment).commitAllowingStateLoss();
        }

        if (playLists != null && playLists.size() > 0 && viewPagerAdapter.getNewVideosFragment() != null) {
            viewPagerAdapter.getPlayListsFragment().populatePlayLists(playLists);
            mainViewPager.getAdapter().notifyDataSetChanged();
            setTabsStyle();
        }
    }

    private void onPlayListItemsLiveChanged(List<PlayListItem> playListItems) {
        if (mFragmentManager != null && mLoadingFragment != null) {
            mFragmentManager.beginTransaction().remove(mLoadingFragment).commitAllowingStateLoss();
        }
        if (playListItems != null && playListItems.size() > 0 && mViewModel.getLastPlayListId() != null) {
            initializePlayListItemsFragment(playListItems, mViewModel.getPlaylistTitle(mViewModel.getLastPlayListId()));
        }
    }

    private void observeVariables() {
        mViewModel.getNewVideosLive().observe(this, this::onNewVideosLiveChanged);

        mViewModel.getAllFavoritesItemsLive().observe(this, (List<Video> videos) -> mViewModel.setFavoritesItems(videos));
        mViewModel.getCurrentItemDescriptionLive().observe(this, this::onVideoDescriptionResponseChanged);
        mViewModel.getCurrentItemStatisticsLive().observe(this, this::onVideosStatisticsResponseChanged);

        mViewModel.getAllPlayListsLive().observe(this, this::onAllPlayListsLiveChanged);
        mViewModel.getPlayListItemsLive().observe(this, this::onPlayListItemsLiveChanged);
    }

    private void startVideoActivity(Video video) {
        Intent videoIntent = new Intent(MainActivity.this, VideoActivity.class);
        videoIntent.putExtra(VideoActivity.ITEM_KEY, new Gson().toJson(video));
        startActivity(videoIntent);
    }


    private void findAllFragments() {
        for (Fragment fragment : mFragmentManager.getFragments()) {
            if (fragment != null && fragment.getTag() != null) {
                switch (fragment.getTag()) {

                    case HomePageFragment.TAG:
                        mHomePageFragment = (HomePageFragment) mFragmentManager.findFragmentByTag(HomePageFragment.TAG);
                        break;

                    case ChangeThemeFragment.TAG:
                        mChangeThemeFragment = (ChangeThemeFragment) mFragmentManager.findFragmentByTag(ChangeThemeFragment.TAG);
                        break;

                    case LoadingFragment.TAG:
                        mLoadingFragment = (LoadingFragment) mFragmentManager.findFragmentByTag(LoadingFragment.TAG);
                        break;

                    case PlayListItemsFragment.PLAY_LIST_ITEMS_FRAGMENT_TAG:
                        mPlayListItemsFragment = (PlayListItemsFragment) mFragmentManager.findFragmentByTag(PlayListItemsFragment.PLAY_LIST_ITEMS_FRAGMENT_TAG);
                        break;

                    case PrivacyPolicyFragment.TAG:
                        mPrivacyPolicyFragment = (PrivacyPolicyFragment) mFragmentManager.findFragmentByTag(PrivacyPolicyFragment.TAG);
                        break;
                }
            }
        }
    }

    @Override
    public void onPlayListClicked(String playListId) {
        initializeLoadingFragment(getString(R.string.loading));
        mViewModel.getPlayListItems().clear();
        mViewModel.loadPlayListVideos(playListId);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (deleteMenuItem != null && deleteMenuItem.isVisible()) {
            abortDeletion();
        }
        else if ((mChangeThemeFragment == null || !mChangeThemeFragment.isAdded()) &&
                (mPrivacyPolicyFragment == null || !mPrivacyPolicyFragment.isAdded()) &&
                (mPlayListItemsFragment == null || !mPlayListItemsFragment.isAdded()) &&
                (mHomePageFragment == null || !mHomePageFragment.isAdded())) {

            DialogInterface.OnClickListener discardButtonClickListener = (DialogInterface dialogInterface, int i) -> finishAffinity();

            showAppClosingConfirmDialog(discardButtonClickListener, getString(R.string.close_app_warning), getString(R.string.close), getString(R.string.back));
        }
        else {
            super.onBackPressed();
        }
    }

    private void abortDeletion() {
        mViewModel.itemsToDelete.clear();
        if (deleteMenuItem.isVisible()) {
            deleteMenuItem.setVisible(false);
        }
        if (viewPagerAdapter != null && viewPagerAdapter.getFavoriteVideosFragment() != null && viewPagerAdapter.getFavoriteVideosFragment().mAdapter != null) {
            for (Video video : viewPagerAdapter.getFavoriteVideosFragment().mAdapter.getAllItems())
                video.setSelected(false);
            viewPagerAdapter.getFavoriteVideosFragment().mAdapter.notifyDataSetChanged();
        }
    }

    // Display an alert dialog
    private void showDeletionConfirmDialog(DialogInterface.OnClickListener deleteButtonClickListener, String message, String positiveButtonCaption, String negativeButtonCaption) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(positiveButtonCaption, deleteButtonClickListener);
        builder.setNegativeButton(negativeButtonCaption, this::onAbortDeletionButtonClicked);
        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void onAbortDeletionButtonClicked(DialogInterface dialog, int id) {
        if (dialog != null) {
            if (mViewModel.itemsToDelete.size() > 0) {
                abortDeletion();
            }
            dialog.dismiss();
        }
    }

    // Display an alert dialog
    private void showAppClosingConfirmDialog(DialogInterface.OnClickListener deleteButtonClickListener, String message, String positiveButtonCaption, String negativeButtonCaption) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(positiveButtonCaption, deleteButtonClickListener);
        builder.setNegativeButton(negativeButtonCaption, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_new_videos) {
            clearAllSecondaryFragments();
            mainViewPager.setCurrentItem(MainViewPagerAdapter.NEW_VIDEOS_FRAGMENT_INDEX);

        } else if (id == R.id.nav_play_lists) {
            clearAllSecondaryFragments();
            mainViewPager.setCurrentItem(MainViewPagerAdapter.PLAY_LISTS_FRAGMENT_INDEX);

        } else if (id == R.id.nav_favorites_videos) {
            clearAllSecondaryFragments();
            mainViewPager.setCurrentItem(MainViewPagerAdapter.FAVORITE_VIDEOS_FRAGMENT_INDEX);

        } else if (id == R.id.nav_change_theme) {
            initializeChangeThemeFragment();
        } else if (id == R.id.nav_settings) {
            showSettings();
        } else if (id == R.id.nav_privacy_policy) {
            initializePrivacyPolicyFragment();
        } else if (id == R.id.nav_home_page) {
            initializeHomePageFragment();
        } else if (id == R.id.nav_rate_app) {
            rateApp();
        } else if (id == R.id.nav_share_app) {
            shareApp();
        } else if (id == R.id.nav_about_us) {
            startAboutUsActivity();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void clearAllSecondaryFragments(){
        if (backMenuItem != null){
            backMenuItem.setVisible(false);
        }
        setTitle(getString(R.string.app_name));
        findAllFragments();
        for (Fragment fragment : mFragmentManager.getFragments()){
            if (fragment != null){
                if (!(fragment instanceof NewVideosFragment) &&
                        !(fragment instanceof PlayListsFragment) &&
                        !(fragment instanceof FavoriteVideosFragment)){
                    mFragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss();
                }
            }
        }
    }

    private void showSettings() {
        Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
        if (settingsIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(settingsIntent);
        }
    }

    private void shareApp() {
        Intent intentInvite = new Intent(Intent.ACTION_SEND);
        intentInvite.setType("text/plain");
        String body = "https://play.google.com/store/apps/details?id=" + getPackageName();
        intentInvite.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        intentInvite.putExtra(Intent.EXTRA_TEXT, body);
        startActivity(Intent.createChooser(intentInvite, "Share using"));
    }

    private void rateApp() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        if (Build.VERSION.SDK_INT >= 21) {
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        }
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    private void startAboutUsActivity() {
        Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void initializeLoadingFragment(String message) {
        mLoadingFragment = new LoadingFragment();

        if (message != null) {
            Bundle bundle = new Bundle();
            bundle.putString(LoadingFragment.MESSAGE_KEY, message);
            mLoadingFragment.setArguments(bundle);
        }
        mFragmentManager.beginTransaction().replace(R.id.main_fragment_container, mLoadingFragment, LoadingFragment.TAG).commitAllowingStateLoss();
    }

    private void initializeHomePageFragment() {
        mHomePageFragment = new HomePageFragment();
        mFragmentManager.beginTransaction().replace(R.id.main_fragment_container, mHomePageFragment, HomePageFragment.TAG).addToBackStack(null).commitAllowingStateLoss();
    }

    private void initializeChangeThemeFragment() {
        mChangeThemeFragment = new ChangeThemeFragment();
        mFragmentManager.beginTransaction().replace(R.id.main_fragment_container, mChangeThemeFragment, ChangeThemeFragment.TAG).addToBackStack(null).commitAllowingStateLoss();
    }

    private void initializePrivacyPolicyFragment() {
        mPrivacyPolicyFragment = new PrivacyPolicyFragment();
        mFragmentManager.beginTransaction().replace(R.id.main_fragment_container, mPrivacyPolicyFragment, PrivacyPolicyFragment.TAG).addToBackStack(null).commitAllowingStateLoss();
    }

    private void initializePlayListItemsFragment(List<PlayListItem> playListItems, String listTitle) {
        mPlayListItemsFragment = new PlayListItemsFragment();
        Bundle bundle = new Bundle();
        if (playListItems != null && playListItems.size() > 0) {
            bundle.putString(PlayListItemsFragment.PLAY_LIST_ITEMS_RESPONSE_KEY, new Gson().toJson(playListItems));
            mPlayListItemsFragment.setArguments(bundle);
        }
        if (listTitle != null) {
            bundle.putString(PlayListItemsFragment.PLAY_LIST_TITLE_KEY, listTitle);
        }
        mFragmentManager.beginTransaction().replace(R.id.main_fragment_container, mPlayListItemsFragment, PlayListItemsFragment.PLAY_LIST_ITEMS_FRAGMENT_TAG).addToBackStack(null).commitAllowingStateLoss();
    }

    @Override
    public void onPlayListItemClicked(Video video) {
        initializeLoadingFragment(getString(R.string.loading));
        mViewModel.setCurrentItem(video);
        mViewModel.loadVideoDescription(video.getId().getVideoId());
    }

    @Override
    public void onFavoriteVideoClicked(Video video) {
        initializeLoadingFragment(getString(R.string.loading));
        mViewModel.setCurrentItem(video);
        mViewModel.loadVideoDescription(video.getId().getVideoId());
    }

    @Override
    public void onFavoriteVideoLongClicked(Video video) {
        if (!deleteMenuItem.isVisible()) {
            deleteMenuItem.setVisible(true);
        }
        if (video.isSelected()) {
            mViewModel.itemsToDelete.remove(video);
            video.setSelected(false);
        } else {
            mViewModel.itemsToDelete.add(video);
            video.setSelected(true);
        }

        viewPagerAdapter.getFavoriteVideosFragment().mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFavoriteVideosFragmentDetached() {
        if (deleteMenuItem != null) {
            deleteMenuItem.setVisible(false);
        }
    }

    @Override
    public void onItemsFragmentAttached() {
        if (backMenuItem != null) {
            backMenuItem.setVisible(true);
        }
    }

    @Override
    public void onItemsFragmentDetached() {
        if (backMenuItem != null) {
            backMenuItem.setVisible(false);
        }
        setTitle(getString(R.string.app_name));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPlayListItemsFragment != null){
            mFragmentManager.beginTransaction().replace(R.id.main_fragment_container, mPlayListItemsFragment, PlayListItemsFragment.PLAY_LIST_ITEMS_FRAGMENT_TAG).commitAllowingStateLoss();
            if (backMenuItem != null){
                backMenuItem.setVisible(true);
            }
        }
        mViewModel.updateFavoriteItems();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void showToastMessage(String message) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        backMenuItem = menu.findItem(R.id.action_back);
        if ((mChangeThemeFragment != null && mChangeThemeFragment.isAdded() ||
                mPrivacyPolicyFragment != null && mPrivacyPolicyFragment.isAdded() ||
                mPlayListItemsFragment != null && mPlayListItemsFragment.isAdded() ||
                mHomePageFragment != null && mHomePageFragment.isAdded())) {
            backMenuItem.setVisible(true);
        }
        deleteMenuItem = menu.findItem(R.id.action_delete);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case R.id.action_back:
                onBackPressed();
                break;

            case R.id.action_delete:
                DialogInterface.OnClickListener discardButtonClickListener = this::onDeleteItemClickedDialog;

                showDeletionConfirmDialog(discardButtonClickListener, getString(R.string.remove_from_favorites_warning), getString(R.string.remove), getString(R.string.cancel));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onDeleteItemClickedDialog(DialogInterface dialogInterface, int i) {
        boolean success = mViewModel.removeFavoriteItems(mViewModel.itemsToDelete);
        mViewModel.updateFavoriteItems();
        if (success) {
            if (deleteMenuItem.isVisible()) {
                deleteMenuItem.setVisible(false);
            }
            if (viewPagerAdapter != null && viewPagerAdapter.getFavoriteVideosFragment() != null && viewPagerAdapter.getFavoriteVideosFragment().mAdapter != null) {
                viewPagerAdapter.getFavoriteVideosFragment().mAdapter.notifyDataSetChanged();
            }
            mViewModel.itemsToDelete.clear();
            showToastMessage(getString(R.string.deletion_success));
        } else {
            if (deleteMenuItem.isVisible()) {
                deleteMenuItem.setVisible(false);
            }

            mViewModel.itemsToDelete.clear();

            viewPagerAdapter.getFavoriteVideosFragment().mAdapter.notifyDataSetChanged();
            showToastMessage(getString(R.string.deletion_failed));
        }
    }

    @Override
    public void onHomePageFragmentAttached() {
        if (backMenuItem != null) {
            backMenuItem.setVisible(true);
        }
        setTitle(getString(R.string.web_site_title));
    }

    @Override
    public void onHomePageFragmentDetached() {
        if (backMenuItem != null) {
            backMenuItem.setVisible(false);
        }
        setTitle(getString(R.string.app_name));
    }

    @Override
    public void onPrivacyPolicyFragmentAttached() {
        if (backMenuItem != null) {
            backMenuItem.setVisible(true);
        }
        setTitle(getString(R.string.privacy_policy_fragment_title));
    }

    @Override
    public void onPrivacyPolicyFragmentDetached() {
        if (backMenuItem != null) {
            backMenuItem.setVisible(false);
        }
        setTitle(getString(R.string.app_name));
    }

    @Override
    public void onChangeThemeFragmentAttached() {
        if (backMenuItem != null) {
            backMenuItem.setVisible(true);
        }
        setTitle(getString(R.string.change_theme_title));
    }

    @Override
    public void onChangeThemeFragmentDetached() {
        if (backMenuItem != null) {
            backMenuItem.setVisible(false);
        }
        setTitle(getString(R.string.app_name));
    }

    @Override
    public void onVideoClicked(Video video) {
        initializeLoadingFragment(getString(R.string.loading));
        mViewModel.setCurrentItem(video);
        mViewModel.loadVideoDescription(video.getId().getVideoId());
    }
}
