package com.kayali_developer.sobhimohammad.main;

import android.app.Application;
import android.widget.Toast;

import com.kayali_developer.sobhimohammad.AppConstants;
import com.kayali_developer.sobhimohammad.R;
import com.kayali_developer.sobhimohammad.data.AppDatabase;
import com.kayali_developer.sobhimohammad.data.model.PlayListItemsResponse;
import com.kayali_developer.sobhimohammad.data.model.PlayListsResponse;
import com.kayali_developer.sobhimohammad.data.model.VideoStatisticsResponse;
import com.kayali_developer.sobhimohammad.data.remote.APIService;
import com.kayali_developer.sobhimohammad.data.remote.ApiUtils;
import com.kayali_developer.sobhimohammad.utilities.AppDateUtils;
import com.kayali_developer.sobhimohammad.utilities.AppExecutors;
import com.kayali_developer.sobhimohammad.utilities.Prefs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MainViewModel extends AndroidViewModel {
    private static final String PART_SNIPPET = "snippet";
    private static final String PART_STATISTIC = "statistics";
    private static final String ORDER = "date";
    private static final int MAX_RESULTS = 50;

    private long mAddedVideosCount = 0;
    private long mRemovedVideosCount = 0;

    private AppDatabase mDb;
    private APIService mService;

    private PlayListItemsResponse mPlaylistItemsResponse;
    private MutableLiveData<PlayListItemsResponse> mPlaylistItemsResponseLive = new MutableLiveData<>();

    private PlayListItemsResponse.Item mCurrentItem = null;
    private MutableLiveData<VideoStatisticsResponse> mCurrentItemStatisticsLive = new MutableLiveData<>();

    private MutableLiveData<List<PlayListsResponse.Item>> mAllPlayListsLive;
    private MutableLiveData<List<PlayListItemsResponse.Item>> mNewVideosLive;
    private MutableLiveData<List<PlayListItemsResponse.Item>> mFavoritesItemsLive;


    private List<PlayListItemsResponse.Item> mFavoritesItems;
    private List<PlayListItemsResponse.Item> mNewVideos;
    private List<PlayListItemsResponse.Item> mAllVideos;
    private List<PlayListsResponse.Item> mAllPlayLists;

    public MainViewModel(@NonNull Application application) {
        super(application);
        mDb = AppDatabase.getInstance(application);
        mAllPlayListsLive = new MutableLiveData<>();
        mNewVideosLive = new MutableLiveData<>();
        mFavoritesItemsLive = new MutableLiveData<>();
        mFavoritesItemsLive.postValue(mDb.videoDao().loadAllVideos());

        mAllVideos = new ArrayList<>();
        mNewVideos = new ArrayList<>();
        mAllPlayLists = new ArrayList<>();

        mService = ApiUtils.getAPIService();

        loadAndUpdateAllVideosNewVideosAllPlayLists();
        //loadPlayLists();

    }

    private void loadAndUpdateAllVideosNewVideosAllPlayLists() {
        mService.getPlaylists(PART_SNIPPET, AppConstants.YOUTUBE_CHANNEL_ID, MAX_RESULTS, AppConstants.YOUTUBE_DATA_V3_API_KEY).enqueue(new Callback<PlayListsResponse>() {
            @Override
            public void onResponse(Call<PlayListsResponse> call, Response<PlayListsResponse> response) {
                if (response.body() != null) {
                    mAllPlayLists = response.body().getItems();
                    for (PlayListsResponse.Item playList : mAllPlayLists) {
                        mService.getPlaylistItems(PART_SNIPPET, playList.getId(), MAX_RESULTS, ORDER, AppConstants.YOUTUBE_DATA_V3_API_KEY).enqueue(new Callback<PlayListItemsResponse>() {
                            @Override
                            public void onResponse(Call<PlayListItemsResponse> call, Response<PlayListItemsResponse> response) {
                                if (response.body() != null) {
                                    mAllVideos.addAll(response.body().getItems());
                                    mNewVideosLive.postValue(getLastVideos(Prefs.getNewVideosCount(getApplication()), mAllVideos));
                                }
                            }

                            @Override
                            public void onFailure(Call<PlayListItemsResponse> call, Throwable t) {
                                showToastMessage(getApplication().getString(R.string.cannot_load_play_lists));
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<PlayListsResponse> call, Throwable t) {
                showToastMessage(getApplication().getString(R.string.cannot_load_play_lists));
            }
        });
    }

    private List<PlayListItemsResponse.Item> getLastVideos(int newVideosCount, List<PlayListItemsResponse.Item> allVideos) {
        if (allVideos != null && allVideos.size() > 0) {
            List<PlayListItemsResponse.Item> allVideosTemp = new ArrayList<>();
            allVideosTemp.addAll(allVideos);
            List<PlayListItemsResponse.Item> lastVideos = new ArrayList<>();
            List<Long> allVideosDateAsLong = new ArrayList<>();
            for (int i = 0; i < allVideosTemp.size(); i++) {
                allVideosDateAsLong.add(AppDateUtils.youtubeFormatToMillis(allVideosTemp.get(i).getSnippet().getPublishedAt()));
            }

            for (int i = 0; i < newVideosCount; i++) {
                if (allVideosDateAsLong.size() > 0) {
                    int maxIndex = allVideosDateAsLong.indexOf(Collections.max(allVideosDateAsLong));
                    lastVideos.add(allVideosTemp.get(maxIndex));
                    allVideosTemp.remove(maxIndex);
                    allVideosDateAsLong.remove(maxIndex);
                }
            }

            if (lastVideos.size() > 0) {
                return lastVideos;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }


    List<PlayListItemsResponse.Item> getPlaylistItems(String playListId){
        List<PlayListItemsResponse.Item> playlistItems = new ArrayList<>();
        for (PlayListItemsResponse.Item item : mAllVideos){
            if (item.getSnippet().getPlaylistId().equals(playListId)){
                playlistItems.add(item);
            }
        }
        return playlistItems;
    }

    public void requestVideoStatistics(String videoId){
        final APIService mService = ApiUtils.getAPIService();
        mService.getVideoStatistics(PART_STATISTIC, videoId, AppConstants.YOUTUBE_DATA_V3_API_KEY).enqueue(new Callback<VideoStatisticsResponse>() {
            @Override
            public void onResponse(Call<VideoStatisticsResponse> call, Response<VideoStatisticsResponse> response) {
                mCurrentItemStatisticsLive.postValue(response.body());
                Timber.e(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + response.body().getItems().size());
            }

            @Override
            public void onFailure(Call<VideoStatisticsResponse> call, Throwable t) {
                showToastMessage(getApplication().getString(R.string.cannot_load_play_lists));
            }
        });
    }

    public boolean isFavorite(String itemId){
        return mDb.videoDao().isFavorite(itemId);
    }

    public boolean addToFavorites(PlayListItemsResponse.Item currentItem){

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mAddedVideosCount = mDb.videoDao().insertVideo(currentItem);
            }
        });

        boolean added = mAddedVideosCount > 0;
        mAddedVideosCount = 0;

        return added;
    }

    public boolean removeFromFavorites(String itemId){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mRemovedVideosCount = mDb.videoDao().deleteVideoByItemId(itemId);
            }
        });
        boolean removed = mRemovedVideosCount > 0;
        mRemovedVideosCount = 0;
        return removed;
    }

    private void showToastMessage(String message) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
    }


    public PlayListItemsResponse.Item getCurrentItem() {
        return mCurrentItem;
    }

    public MutableLiveData<VideoStatisticsResponse> getCurrentItemStatisticsLive() {
        return mCurrentItemStatisticsLive;
    }


    public MutableLiveData<List<PlayListsResponse.Item>> getAllPlayListsLive() {
        return mAllPlayListsLive;
    }

    public MutableLiveData<List<PlayListItemsResponse.Item>> getNewVideosLive() {
        return mNewVideosLive;
    }

    public MutableLiveData<List<PlayListItemsResponse.Item>> getFavoritesItemsLive() {
        return mFavoritesItemsLive;
    }

    public List<PlayListItemsResponse.Item> getFavoritesItems() {
        return mFavoritesItems;
    }

    public List<PlayListItemsResponse.Item> getNewVideos() {
        return mNewVideos;
    }

    public List<PlayListItemsResponse.Item> getAllVideos() {
        return mAllVideos;
    }

    public List<PlayListsResponse.Item> getAllPlayLists() {
        return mAllPlayLists;
    }

    public MutableLiveData<List<PlayListItemsResponse.Item>> getAllFavoritesItemsLive(){
        return mFavoritesItemsLive;
    }



    public void setFavoritesItems(List<PlayListItemsResponse.Item> mFavoritesItems) {
        this.mFavoritesItems = mFavoritesItems;
    }

    public void setNewVideos(List<PlayListItemsResponse.Item> mNewVideos) {
        this.mNewVideos = mNewVideos;
    }

    public void setCurrentItem(PlayListItemsResponse.Item mCurrentItem) {
        this.mCurrentItem = mCurrentItem;
    }
}
