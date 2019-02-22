package com.kayali_developer.sobhimohammad.main;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.kayali_developer.sobhimohammad.LoadingFragment;
import com.kayali_developer.sobhimohammad.R;
import com.kayali_developer.sobhimohammad.data.model.PlayListItemsResponse;
import com.kayali_developer.sobhimohammad.data.model.PlayListsResponse;
import com.kayali_developer.sobhimohammad.data.model.VideoStatisticsResponse;

import java.util.List;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        PlayListsFragment.PlayListsFragmentListener,
        ItemsFragment.ItemsFragmentListener,
        FavoritesVideosFragment.FavoritesVideosFragmentListener,
        ItemFragment.ItemFragmentListener {

    private Context mContext;
    MainViewModel mViewModel;
    FragmentManager mFragmentManager;

    private LoadingFragment mLoadingFragment;


    private ItemFragment mItemFragment;
    FavoritesVideosFragment mFavoritesVideosFragment;
    PlayListsFragment mPlayListsFragment;
    ItemsFragment mItemsFragment;
    private AboutUsFragment mAboutUsFragment;
    private HomePageFragment mHomePageFragment;
    private ChangeThemeFragment mChangeThemeFragment;
    private MainFragment mMainFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Timber.plant(new Timber.DebugTree());
        mContext = MainActivity.this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
            String newToken = instanceIdResult.getToken();
            Log.e("newToken", newToken);

        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            //initializeLoadingFragment(getString(R.string.loading));
        }
        mFragmentManager = getSupportFragmentManager();
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        observeVariables();

    }



    private void observeVariables() {
        mViewModel.getNewVideosLive().observe(this, new Observer<List<PlayListItemsResponse.Item>>() {
            @Override
            public void onChanged(List<PlayListItemsResponse.Item> items) {
                mViewModel.setNewVideos(items);
                if (items != null && items.size() > 0 && mItemsFragment == null && mPlayListsFragment == null && mFavoritesVideosFragment == null) {
                    initializeNewVideosFragment();
                    initializePlayListsFragment();
                    initializeFavoritesVideosFragment();
                    initializeMainFragment();
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
                    initializeItemFragment(new Gson().toJson(mViewModel.getCurrentItem()), new Gson().toJson(videoStatisticsResponse));
                }
            }
        });
    }



    private void initializeLoadingFragment(String message) {
        mLoadingFragment = new LoadingFragment();
        if (message != null) {
            Bundle bundle = new Bundle();
            bundle.putString(LoadingFragment.MESSAGE_KEY, message);
            mLoadingFragment.setArguments(bundle);
        }
        //mFragmentManager.beginTransaction().replace(R.id.main_fragment_container, mLoadingFragment, LoadingFragment.TAG).commitAllowingStateLoss();
    }


    @Override
    public void onPlayListItemClicked(String playListId) {
        initializeItemsFragment(mViewModel.getPlaylistItems(playListId));
    }

    private void initializeNewVideosFragment() {
        mItemsFragment = new ItemsFragment();
        if (mViewModel.getNewVideos() != null && mViewModel.getNewVideos().size() > 0) {
            PlayListItemsResponse playListItemsResponse = new PlayListItemsResponse();
            playListItemsResponse.setItems(mViewModel.getNewVideos());
            Bundle bundle = new Bundle();
            bundle.putString(ItemsFragment.PLAY_LIST_ITEMS_RESPONSE_KEY, new Gson().toJson(playListItemsResponse));
            mItemsFragment.setArguments(bundle);
        }
    }


    private void initializePlayListsFragment() {
        mPlayListsFragment = PlayListsFragment.newInstance();
        if (mViewModel.getAllPlayLists() != null && mViewModel.getAllPlayLists().size() > 0) {
            PlayListsResponse playListsResponse = new PlayListsResponse();
            playListsResponse.setItems(mViewModel.getAllPlayLists());
            Bundle bundle = new Bundle();
            bundle.putString(PlayListsFragment.PLAY_LISTS_KEY, new Gson().toJson(playListsResponse));
            mPlayListsFragment.setArguments(bundle);
        }
    }

    private void initializeFavoritesVideosFragment() {
        mFavoritesVideosFragment = FavoritesVideosFragment.newInstance();
        if (mViewModel.getFavoritesItems() != null && mViewModel.getFavoritesItems().size() > 0) {
            PlayListItemsResponse playListItemsResponse = new PlayListItemsResponse();
            playListItemsResponse.setItems(mViewModel.getFavoritesItems());
            Bundle bundle = new Bundle();
            bundle.putString(FavoritesVideosFragment.PLAY_LIST_FAVORITES_ITEMS_RESPONSE_KEY, new Gson().toJson(playListItemsResponse));
            mFavoritesVideosFragment.setArguments(bundle);
        }
    }

    private void initializeItemsFragment(List<PlayListItemsResponse.Item> items) {
        mItemsFragment = ItemsFragment.newInstance();
        Bundle bundle = new Bundle();
        PlayListItemsResponse playListItemsResponse = new PlayListItemsResponse();
        playListItemsResponse.setItems(items);
        bundle.putString(ItemsFragment.PLAY_LIST_ITEMS_RESPONSE_KEY, new Gson().toJson(playListItemsResponse));
        mItemsFragment.setArguments(bundle);
        mFragmentManager.beginTransaction().add(R.id.main_fragment_container, mItemsFragment, PlayListsFragment.TAG).commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (mItemFragment != null) {
                getFragmentManager().beginTransaction().remove(mItemFragment).commitAllowingStateLoss();
            }

            if (mItemsFragment != null) {
                mFragmentManager.beginTransaction().remove(mItemsFragment).commitAllowingStateLoss();
            } else {
                super.onBackPressed();
            }
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_new_videos) {
            if (mMainFragment != null){
                mMainFragment.mainViewPager.setCurrentItem(0, true);
            }

        } else if (id == R.id.nav_play_lists) {
            if (mMainFragment != null){
                mMainFragment.mainViewPager.setCurrentItem(1, true);
            }

        } else if (id == R.id.nav_favorites_videos) {
            if (mMainFragment != null){
                mMainFragment.mainViewPager.setCurrentItem(2, true);
            }

        } else if (id == R.id.nav_change_theme) {
            changeTheme();
        } else if (id == R.id.nav_settings) {
            showSettings();
        } else if (id == R.id.nav_home_page) {
            showHomePage();
        } else if (id == R.id.nav_rate_app) {
            rateApp();
        } else if (id == R.id.nav_share_app) {
            shareApp();
        } else if (id == R.id.nav_about_us) {
            showAboutUs();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initializeMainFragment(){
        mMainFragment = MainFragment.newInstance();
        mFragmentManager.beginTransaction().replace(R.id.main_fragment_container, mMainFragment, MainFragment.TAG).commitAllowingStateLoss();
    }

    private void changeTheme(){
        mChangeThemeFragment = ChangeThemeFragment.newInstance();
        mFragmentManager.beginTransaction().replace(R.id.main_fragment_container, mChangeThemeFragment, ChangeThemeFragment.TAG).commitAllowingStateLoss();
    }

    private void showSettings(){
        Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
        if (settingsIntent.resolveActivity(getPackageManager()) != null){
            startActivity(settingsIntent);
        }
    }

    private void showHomePage(){
        mHomePageFragment = HomePageFragment.newInstance();
        mFragmentManager.beginTransaction().replace(R.id.main_fragment_container, mHomePageFragment, HomePageFragment.TAG).commitAllowingStateLoss();
    }

    private void shareApp(){

    }

    private void showAboutUs(){
        mAboutUsFragment = AboutUsFragment.newInstance();
        mFragmentManager.beginTransaction().replace(R.id.main_fragment_container, mAboutUsFragment, AboutUsFragment.TAG).commitAllowingStateLoss();
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


    private void shareVideo() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "https://www.youtube.com/watch?v=" + mViewModel.getCurrentItem().getSnippet().getResourceId().getVideoId();
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    private void removeAddToFavorites() {
        if (mViewModel.isFavorite(mViewModel.getCurrentItem().getId())) {
            if (mItemFragment != null){
                mItemFragment.ivFavorite.setImageResource(R.drawable.ic_heart_outline);
            }
            mViewModel.removeFromFavorites(mViewModel.getCurrentItem().getId());
            showToastMessage("Removed");
        } else {
            if (mItemFragment != null){
                mItemFragment.ivFavorite.setImageResource(R.drawable.ic_heart);
            }
            mViewModel.addToFavorites(mViewModel.getCurrentItem());
            showToastMessage("Added");
        }
    }

    private void initializeItemFragment(String itemJson, String videoStatisticsResponseJson) {
        mItemFragment = new ItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ItemFragment.ITEM_KEY, itemJson);
        bundle.putString(ItemFragment.VIDEO_STATISTICS_KEY, videoStatisticsResponseJson);
        mItemFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().add(R.id.main_fragment_container, mItemFragment, ItemFragment.TAG).commitAllowingStateLoss();
    }

    private void showToastMessage(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPlayListItemClicked(PlayListItemsResponse.Item item) {
        mViewModel.setCurrentItem(item);
        mViewModel.requestVideoStatistics(item.getSnippet().getResourceId().getVideoId());
    }

    @Override
    public void onRemoveAddToFavoritesClicked() {
        removeAddToFavorites();
    }

    @Override
    public void onShareVideoClicked() {
        shareVideo();
    }
}
