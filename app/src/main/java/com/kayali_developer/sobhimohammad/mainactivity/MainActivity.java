package com.kayali_developer.sobhimohammad.mainactivity;


import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.kayali_developer.sobhimohammad.LoadingFragment;
import com.kayali_developer.sobhimohammad.R;
import com.kayali_developer.sobhimohammad.aboutus.AboutUsActivity;
import com.kayali_developer.sobhimohammad.data.model.PlayListItemsResponse;
import com.kayali_developer.sobhimohammad.data.model.VideoStatisticsResponse;
import com.kayali_developer.sobhimohammad.mainfragment.FavoriteVideosFragment;
import com.kayali_developer.sobhimohammad.mainfragment.ItemsFragment;
import com.kayali_developer.sobhimohammad.mainfragment.MainFragment;
import com.kayali_developer.sobhimohammad.mainfragment.NewVideosFragment;
import com.kayali_developer.sobhimohammad.mainfragment.PlayListsFragment;
import com.kayali_developer.sobhimohammad.settings.SettingsActivity;
import com.kayali_developer.sobhimohammad.videoactivity.VideoActivity;

import java.util.List;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity

        extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        PlayListsFragment.PlayListsFragmentListener,
        ItemsFragment.ItemsFragmentListener,
        MainFragment.MainFragmentListener,
        FavoriteVideosFragment.FavoriteVideosFragmentListener,
        ChangeThemeFragment.ChangeThemeFragmentListener,
        HomePageFragment.HomePageFragmentListener,
        PrivacyPolicyFragment.PrivacyPolicyFragmentListener,
        NewVideosFragment.NewVideosFragmentListener {

    public MainViewModel mViewModel;
    public FragmentManager mFragmentManager;
    private LoadingFragment mLoadingFragment;
    private HomePageFragment mHomePageFragment;
    private ChangeThemeFragment mChangeThemeFragment;
    public MainFragment mMainFragment;
    private ItemsFragment mPlayListItemsFragment;
    private PrivacyPolicyFragment mPrivacyPolicyFragment;

    MenuItem backMenuItem;
    MenuItem deleteMenuItem;
    public Toolbar mToolbar;
    public NavigationView mNavigationView;
    public DrawerLayout mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Timber.plant(new Timber.DebugTree());
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
        mFragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            initializeLoadingFragment(getString(R.string.loading));
        }
        findAllFragments();
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        observeVariables();

    }


    private void observeVariables() {
        mViewModel.getNewVideosLive().observe(this, new Observer<List<PlayListItemsResponse.Item>>() {
            @Override
            public void onChanged(List<PlayListItemsResponse.Item> items) {
                mViewModel.setNewVideos(items);
                if (items != null && items.size() > 0) {
                    initializeMainFragment(mViewModel.lastSelectedTabInMainFragment);
                }
            }
        });

        mViewModel.getAllFavoritesItemsLive().observe(this, new Observer<List<PlayListItemsResponse.Item>>() {
            @Override
            public void onChanged(List<PlayListItemsResponse.Item> items) {
                mViewModel.setFavoritesItems(items);
            }
        });

        mViewModel.getCurrentItemStatisticsLive().observe(this, new Observer<VideoStatisticsResponse>() {
            @Override
            public void onChanged(VideoStatisticsResponse videoStatisticsResponse) {
                if (videoStatisticsResponse != null && videoStatisticsResponse.getItems() != null &&
                        videoStatisticsResponse.getItems().size() > 0 && videoStatisticsResponse.getItems().get(0) != null &&
                        videoStatisticsResponse.getItems().get(0).getStatistics() != null
                ) {
                    mFragmentManager.beginTransaction().remove(mLoadingFragment).commitAllowingStateLoss();
                    startVideoActivity(videoStatisticsResponse);
                }
            }
        });
    }

    private void startVideoActivity(VideoStatisticsResponse videoStatisticsResponse) {
        Intent videoIntent = new Intent(MainActivity.this, VideoActivity.class);
        videoIntent.putExtra(VideoActivity.ITEM_KEY, new Gson().toJson(mViewModel.getCurrentItem()));
        videoIntent.putExtra(VideoActivity.VIDEO_STATISTICS_KEY, new Gson().toJson(videoStatisticsResponse));
        startActivity(videoIntent);
    }


    private void findAllFragments() {
        for (Fragment fragment : mFragmentManager.getFragments()) {
            if (fragment != null && fragment.getTag() != null) {
                switch (fragment.getTag()) {
                    case MainFragment.TAG:
                        mMainFragment = (MainFragment) mFragmentManager.findFragmentByTag(MainFragment.TAG);
                        break;

                    case HomePageFragment.TAG:
                        mHomePageFragment = (HomePageFragment) mFragmentManager.findFragmentByTag(HomePageFragment.TAG);
                        break;

                    case ChangeThemeFragment.TAG:
                        mChangeThemeFragment = (ChangeThemeFragment) mFragmentManager.findFragmentByTag(ChangeThemeFragment.TAG);
                        break;

                    case LoadingFragment.TAG:
                        mLoadingFragment = (LoadingFragment) mFragmentManager.findFragmentByTag(LoadingFragment.TAG);
                        break;

                    case ItemsFragment.PLAY_LIST_ITEMS_FRAGMENT_TAG:
                        mPlayListItemsFragment = (ItemsFragment) mFragmentManager.findFragmentByTag(ItemsFragment.PLAY_LIST_ITEMS_FRAGMENT_TAG);
                        break;

                    case PlayListsFragment.TAG:
                        if (mMainFragment != null) {
                            mMainFragment.mPlayListsFragment = (PlayListsFragment) mFragmentManager.findFragmentByTag(PlayListsFragment.TAG);
                        }
                        break;

                    case FavoriteVideosFragment.TAG:
                        if (mMainFragment != null) {
                            mMainFragment.mFavoriteVideosFragment = (FavoriteVideosFragment) mFragmentManager.findFragmentByTag(FavoriteVideosFragment.TAG);
                        }
                        break;

                    case NewVideosFragment.TAG:
                        if (mMainFragment != null) {
                            mMainFragment.mNewVideosFragment = (NewVideosFragment) mFragmentManager.findFragmentByTag(NewVideosFragment.TAG);
                        }

                    case PrivacyPolicyFragment.TAG:
                        mPrivacyPolicyFragment = (PrivacyPolicyFragment) mFragmentManager.findFragmentByTag(PrivacyPolicyFragment.TAG);
                        break;
                }
            }
        }
    }

    @Override
    public void onPlayListItemClicked(String playListId) {
        initializePlayListItemsFragment(mViewModel.getPlaylistItems(playListId), mViewModel.getPlaylistTitle(playListId));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if ((mChangeThemeFragment == null || !mChangeThemeFragment.isAdded()) &&
                (mPrivacyPolicyFragment == null || !mPrivacyPolicyFragment.isAdded()) &&
                (mPlayListItemsFragment == null || !mPlayListItemsFragment.isAdded()) &&
                (mHomePageFragment == null || !mHomePageFragment.isAdded())) {

            DialogInterface.OnClickListener discardButtonClickListener =
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finishAffinity();
                        }
                    };
            showAppClosingConfirmDialog(discardButtonClickListener, getString(R.string.close_app_warning), getString(R.string.close), getString(R.string.back));

        } else {
            super.onBackPressed();
        }
    }

    private void abortDeletion() {
        mViewModel.itemsToDelete.clear();
        if (deleteMenuItem.isVisible()) {
            deleteMenuItem.setVisible(false);
        }
        if (mMainFragment != null && mMainFragment.mFavoriteVideosFragment != null && mMainFragment.mFavoriteVideosFragment.mAdapter != null) {
            for (PlayListItemsResponse.Item item : mMainFragment.mFavoriteVideosFragment.mAdapter.getAllItems())
                item.setSelected(false);
            mMainFragment.mFavoriteVideosFragment.mAdapter.notifyDataSetChanged();
        }
    }

    // Display an alert dialog
    private void showDeletionConfirmDialog(DialogInterface.OnClickListener deleteButtonClickListener, String message, String positiveButtonCaption, String negativeButtonCaption) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(positiveButtonCaption, deleteButtonClickListener);
        builder.setNegativeButton(negativeButtonCaption, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    if (mViewModel.itemsToDelete.size() > 0) {
                        abortDeletion();
                    }
                    dialog.dismiss();
                }
            }
        });
        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
            if (mMainFragment == null || !mMainFragment.isDetached()) {
                initializeMainFragment(MainFragment.NEW_VIDEOS_TAB_INDEX);
            } else {
                mMainFragment.showNewVideosFragment();
            }

        } else if (id == R.id.nav_play_lists) {
            if (mMainFragment == null || !mMainFragment.isDetached()) {
                initializeMainFragment(MainFragment.PLAY_LISTS_TAB_INDEX);
            } else {
                mMainFragment.showPlayListsFragment();
            }

        } else if (id == R.id.nav_favorites_videos) {
            if (mMainFragment == null || !mMainFragment.isDetached()) {
                initializeMainFragment(MainFragment.FAVORITE_VIDEOS_TAB_INDEX);
            } else {
                mMainFragment.showFavoritesVideosFragment();
            }

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

    private void initializeMainFragment(int tabIndex) {
        mViewModel.lastSelectedTabInMainFragment = tabIndex;
        mMainFragment = MainFragment.newInstance();
        mFragmentManager.beginTransaction().replace(R.id.main_fragment_container, mMainFragment, MainFragment.TAG).commitAllowingStateLoss();
    }

    private void initializeHomePageFragment() {
        mHomePageFragment = new HomePageFragment();
        mFragmentManager.beginTransaction().replace(R.id.main_fragment_container, mHomePageFragment, HomePageFragment.TAG).addToBackStack(null).commitAllowingStateLoss();
    }

    private void initializeChangeThemeFragment() {
        mChangeThemeFragment = new ChangeThemeFragment();
        mFragmentManager.beginTransaction().replace(R.id.main_fragment_container, mChangeThemeFragment, ChangeThemeFragment.TAG).addToBackStack(null).commitAllowingStateLoss();
        mViewModel.lastFragmentTag = ChangeThemeFragment.TAG;
    }

    private void initializePrivacyPolicyFragment() {
        mPrivacyPolicyFragment = new PrivacyPolicyFragment();
        mFragmentManager.beginTransaction().replace(R.id.main_fragment_container, mPrivacyPolicyFragment, PrivacyPolicyFragment.TAG).addToBackStack(null).commitAllowingStateLoss();
        mViewModel.lastFragmentTag = PrivacyPolicyFragment.TAG;
    }

    private void initializePlayListItemsFragment(List<PlayListItemsResponse.Item> items, String listTitle) {
        mPlayListItemsFragment = new ItemsFragment();
        Bundle bundle = new Bundle();
        if (items != null && items.size() > 0) {
            bundle.putString(ItemsFragment.PLAY_LIST_ITEMS_RESPONSE_KEY, new Gson().toJson(items));
            mPlayListItemsFragment.setArguments(bundle);
        }
        if (listTitle != null) {
            bundle.putString(ItemsFragment.PLAY_LIST_TITLE_KEY, listTitle);
        }
        mFragmentManager.beginTransaction().add(R.id.main_fragment_container, mPlayListItemsFragment, ItemsFragment.PLAY_LIST_ITEMS_FRAGMENT_TAG).addToBackStack(null).commitAllowingStateLoss();
        mViewModel.lastFragmentTag = ItemsFragment.PLAY_LIST_ITEMS_FRAGMENT_TAG;
    }

    @Override
    public void onPlayListItemClicked(PlayListItemsResponse.Item item) {
        initializeLoadingFragment(getString(R.string.loading));
        mViewModel.setCurrentItem(item);
        mViewModel.requestVideoStatistics(item.getSnippet().getResourceId().getVideoId());
    }

    @Override
    public void onPlayListItemLongClicked(PlayListItemsResponse.Item item) {
        if (!deleteMenuItem.isVisible()) {
            deleteMenuItem.setVisible(true);
        }
        if (item.getSelected()) {
            mViewModel.itemsToDelete.remove(item);
            item.setSelected(false);
        } else {
            mViewModel.itemsToDelete.add(item);
            item.setSelected(true);
        }

        mMainFragment.mFavoriteVideosFragment.mAdapter.notifyDataSetChanged();
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
    public void onNewVideosTabClicked() {
        if (mMainFragment != null) {
            mMainFragment.showNewVideosFragment();
        }
    }

    @Override
    public void onPlayListsTabClicked() {
        if (mMainFragment != null) {
            mMainFragment.showPlayListsFragment();
        }
    }

    @Override
    public void onFavoritesVideosTabClicked() {
        if (mMainFragment != null) {
            mViewModel.updateFavoriteItems();
            mMainFragment.showFavoritesVideosFragment();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.updateFavoriteItems();
        switch (mViewModel.lastFragmentTag) {

            case ChangeThemeFragment.TAG:
                if (mChangeThemeFragment != null){
                    mFragmentManager.beginTransaction().replace(R.id.main_fragment_container, mChangeThemeFragment, ChangeThemeFragment.TAG).commitAllowingStateLoss();

                }                break;

            case PrivacyPolicyFragment.TAG:
                if (mPrivacyPolicyFragment != null){
                    mFragmentManager.beginTransaction().replace(R.id.main_fragment_container, mPrivacyPolicyFragment, PrivacyPolicyFragment.TAG).commitAllowingStateLoss();

                }                break;

            case HomePageFragment.TAG:
                if (mHomePageFragment != null){
                    mFragmentManager.beginTransaction().replace(R.id.main_fragment_container, mHomePageFragment, HomePageFragment.TAG).commitAllowingStateLoss();

                }                break;

            default:
                if (mMainFragment != null){
                    mFragmentManager.beginTransaction().replace(R.id.main_fragment_container, mMainFragment, MainFragment.TAG).commitAllowingStateLoss();

                }
                break;
        }
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
                mHomePageFragment != null && mHomePageFragment.isAdded())){
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
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                boolean success = mViewModel.removeFavoriteItems(mViewModel.itemsToDelete);
                                mViewModel.updateFavoriteItems();
                                if (success) {
                                    if (mMainFragment != null && mMainFragment.mFavoriteVideosFragment != null && mMainFragment.mFavoriteVideosFragment.mAdapter != null) {
                                        mMainFragment.mFavoriteVideosFragment.mAdapter.notifyDataSetChanged();
                                    }
                                    mViewModel.itemsToDelete.clear();
                                    showToastMessage(getString(R.string.deletion_success));
                                } else {
                                    showToastMessage(getString(R.string.deletion_failed));
                                }
                            }
                        };
                showDeletionConfirmDialog(discardButtonClickListener, getString(R.string.remove_from_favorites_warning), getString(R.string.remove), getString(R.string.cancel));
                break;
        }
        return super.onOptionsItemSelected(item);
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
}
