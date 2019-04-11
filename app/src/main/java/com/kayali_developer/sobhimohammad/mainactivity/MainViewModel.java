package com.kayali_developer.sobhimohammad.mainactivity;

import android.app.Application;
import android.widget.Toast;

import com.kayali_developer.sobhimohammad.AppConstants;
import com.kayali_developer.sobhimohammad.R;
import com.kayali_developer.sobhimohammad.data.AppDatabase;
import com.kayali_developer.sobhimohammad.data.model.PlayList;
import com.kayali_developer.sobhimohammad.data.model.PlayListItem;
import com.kayali_developer.sobhimohammad.data.model.PlayListItemsResponse;
import com.kayali_developer.sobhimohammad.data.model.PlayListsResponse;
import com.kayali_developer.sobhimohammad.data.model.Video;
import com.kayali_developer.sobhimohammad.data.model.VideoDescriptionResponse;
import com.kayali_developer.sobhimohammad.data.model.VideoStatisticsResponse;
import com.kayali_developer.sobhimohammad.data.model.getNewVideosResponse;
import com.kayali_developer.sobhimohammad.data.remote.APIService;
import com.kayali_developer.sobhimohammad.data.remote.ApiUtils;
import com.kayali_developer.sobhimohammad.utilities.AppDateUtils;
import com.kayali_developer.sobhimohammad.utilities.Prefs;
import com.kayali_developer.sobhimohammad.utilities.SingleEventMutableLive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends AndroidViewModel {

    static final String PART_SNIPPET = "snippet";
    private static final String PART_STATISTIC = "statistics";
    private static final String ORDER = "date";
    private static final int MAX_RESULTS = 50;

    private static final String FIELDS_GET_ALL_VIDEOS = "items(id(playlistId%2CvideoId)%2Csnippet(description%2CpublishedAt%2Cthumbnails%2Fhigh%2Ctitle))%2CnextPageToken";
    private static final String FIELDS_GET_PLAY_LISTS = "items(id%2Csnippet(description%2CpublishedAt%2Cthumbnails%2Fhigh%2Ctitle))%2CnextPageToken";
    private static final String FIELDS_GET_PLAY_LIST_ITEMS = "items(snippet(description%2CpublishedAt%2CresourceId(playlistId%2CvideoId)%2Cthumbnails%2Fhigh%2Ctitle))%2CnextPageToken";
    private static final String FIELDS_GET_VIDEO_STATISTICS = "items(statistics(dislikeCount%2ClikeCount%2CviewCount))";
    static final String FIELDS_GET_VIDEO_DESCRIPTION = "items%2Fsnippet%2Fdescription";

    private int newVideosToLoadCount;

    private String nextPageToken = null;
    private String nextPageTokenPlayListItems = null;
    private String nextPageTokenPlayLists = null;

    private String lastPlayListId = null;

    private AppDatabase mDb;
    private APIService mService;

    private Video mCurrentVideo = null;
    private SingleEventMutableLive<VideoStatisticsResponse> mCurrentItemStatisticsLive;
    private SingleEventMutableLive<String> mCurrentItemDescriptionLive;

    private MutableLiveData<List<PlayList>> mAllPlayListsLive;
    private SingleEventMutableLive<List<Video>> mNewVideosLive;
    private SingleEventMutableLive<List<PlayListItem>> mPlayListItemsLive;
    private MutableLiveData<List<Video>> mFavoritesItemsLive;

    private List<Video> mFavoritesVideos;
    private List<Video> mNewVideos;
    private List<PlayListItem> mPlayListItems;
    private List<PlayList> mAllPlayLists;

    List<Video> itemsToDelete = new ArrayList<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
        mDb = AppDatabase.getInstance(application);
        mAllPlayListsLive = new MutableLiveData<>();
        mNewVideosLive = new SingleEventMutableLive<>();
        mPlayListItemsLive = new SingleEventMutableLive<>();
        mFavoritesItemsLive = new MutableLiveData<>();
        mFavoritesItemsLive.postValue(mDb.videoDao().loadAllFavoriteVideos());

        mCurrentItemStatisticsLive = new SingleEventMutableLive<>();
        mCurrentItemDescriptionLive = new SingleEventMutableLive<>();

        mNewVideos = new ArrayList<>();
        mPlayListItems = new ArrayList<>();
        mAllPlayLists = new ArrayList<>();

        mService = ApiUtils.getAPIService();

        newVideosToLoadCount = Prefs.getNewVideosCount(getApplication());

        loadNewVideos();
    }

    private void loadNewVideos() {
        if (newVideosToLoadCount > MAX_RESULTS){
            newVideosToLoadCount = MAX_RESULTS;
        }
        mService.getAllVideos(PART_SNIPPET, AppConstants.YOUTUBE_CHANNEL_ID, FIELDS_GET_ALL_VIDEOS, newVideosToLoadCount,
                ORDER,
                AppConstants.YOUTUBE_DATA_V3_API_KEY,
                nextPageToken
        ).enqueue(new Callback<getNewVideosResponse>() {
            @Override
            public void onResponse(Call<getNewVideosResponse> call, Response<getNewVideosResponse> response) {
                if (response.body() != null) {
                    nextPageToken = response.body().getNextPageToken();
                    if (response.body().getVideos() != null) {
                        mNewVideos.addAll(response.body().getVideos());
                        newVideosToLoadCount = Prefs.getNewVideosCount(getApplication()) - mNewVideos.size();
                    }

                    if (nextPageToken == null || newVideosToLoadCount <= 0) {
                        mNewVideosLive.postValue(getLastVideos(Prefs.getNewVideosCount(getApplication()), mNewVideos));
                    } else {
                        loadNewVideos();
                    }

                }
            }

            @Override
            public void onFailure(Call<getNewVideosResponse> call, Throwable t) {
                showToastMessage(getApplication().getString(R.string.cannot_load_new_videos));
            }
        });
    }

    void loadAllPlayLists() {
        mService.getPlayLists(PART_SNIPPET, AppConstants.YOUTUBE_CHANNEL_ID, MAX_RESULTS, FIELDS_GET_PLAY_LISTS, nextPageTokenPlayLists, AppConstants.YOUTUBE_DATA_V3_API_KEY).enqueue(new Callback<PlayListsResponse>() {
            @Override
            public void onResponse(Call<PlayListsResponse> call, Response<PlayListsResponse> response) {
                if (response.body() != null) {
                    nextPageTokenPlayLists = response.body().getNextPageToken();

                    if (response.body().getItems() != null) {
                        mAllPlayLists.addAll(response.body().getItems());
                    }

                    if (nextPageTokenPlayLists == null) {
                        mAllPlayListsLive.postValue(mAllPlayLists);
                    } else {
                        loadAllPlayLists();
                    }
                }
            }

            @Override
            public void onFailure(Call<PlayListsResponse> call, Throwable t) {
                showToastMessage(getApplication().getString(R.string.cannot_load_play_lists));
            }
        });
    }

    void loadPlayListVideos(String playListId) {
        mService.getPlaylistItems(
                PART_SNIPPET,
                playListId,
                MAX_RESULTS,
                FIELDS_GET_PLAY_LIST_ITEMS,
                nextPageTokenPlayListItems,
                AppConstants.YOUTUBE_DATA_V3_API_KEY
                ).enqueue(new Callback<PlayListItemsResponse>() {
            @Override
            public void onResponse(Call<PlayListItemsResponse> call, Response<PlayListItemsResponse> response) {
                if (response.body() != null) {
                    nextPageTokenPlayListItems = response.body().getNextPageToken();
                    lastPlayListId = playListId;

                    if (response.body().getItems() != null) {
                        mPlayListItems.addAll(response.body().getItems());
                    }

                    if (nextPageTokenPlayListItems == null) {
                        mPlayListItemsLive.postValue(mPlayListItems);
                    } else {
                        loadPlayListVideos(playListId);
                    }
                }
            }

            @Override
            public void onFailure(Call<PlayListItemsResponse> call, Throwable t) {
                showToastMessage(getApplication().getString(R.string.cannot_load_play_list_videos));
            }
        });

    }

    void loadVideoDescription(String videoId){
        mService.getVideoDescription(
                PART_SNIPPET,
                videoId,
                FIELDS_GET_VIDEO_DESCRIPTION,
                AppConstants.YOUTUBE_DATA_V3_API_KEY
        ).enqueue(new Callback<VideoDescriptionResponse>() {
            @Override
            public void onResponse(Call<VideoDescriptionResponse> call, Response<VideoDescriptionResponse> response) {
                if (response.body() != null && response.body().getItems() != null && response.body().getItems().size() > 0
                &&response.body().getItems().get(0).getSnippet() != null ) {
                    mCurrentItemDescriptionLive.postValue(response.body().getItems().get(0).getSnippet().getDescription());
                }
            }

            @Override
            public void onFailure(Call<VideoDescriptionResponse> call, Throwable t) {
                showToastMessage(getApplication().getString(R.string.cannot_load_description));
            }
        });
    }


    private List<Video> getLastVideos(int newVideosCount, List<Video> allVideos) {
        if (allVideos != null && allVideos.size() > 0) {
            List<Video> allVideosTemp = new ArrayList<>();
            allVideosTemp.addAll(allVideos);
            List<Video> lastVideos = new ArrayList<>();
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



    String getPlaylistTitle(String playListId) {
        String title = null;
        for (PlayList playlist : mAllPlayLists) {
            if (playlist.getId().equals(playListId)) {
                title = playlist.getSnippet().getTitle();
            }
        }
        return title;
    }

    void requestVideoStatistics(String videoId) {
        final APIService mService = ApiUtils.getAPIService();
        mService.getVideoStatistics(PART_STATISTIC, videoId, FIELDS_GET_VIDEO_STATISTICS, AppConstants.YOUTUBE_DATA_V3_API_KEY).enqueue(new Callback<VideoStatisticsResponse>() {
            @Override
            public void onResponse(Call<VideoStatisticsResponse> call, Response<VideoStatisticsResponse> response) {
                mCurrentItemStatisticsLive.postValue(response.body());
            }

            @Override
            public void onFailure(Call<VideoStatisticsResponse> call, Throwable t) {
                showToastMessage(getApplication().getString(R.string.cannot_load_play_lists));
            }
        });
    }

    void updateFavoriteItems() {
        mFavoritesItemsLive.setValue(mDb.videoDao().loadAllFavoriteVideos());
    }


    private void showToastMessage(String message) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
    }


    Video getCurrentItem() {
        return mCurrentVideo;
    }

    MutableLiveData<VideoStatisticsResponse> getCurrentItemStatisticsLive() {
        return mCurrentItemStatisticsLive;
    }


    MutableLiveData<List<PlayList>> getAllPlayListsLive() {
        return mAllPlayListsLive;
    }

    MutableLiveData<List<Video>> getNewVideosLive() {
        return mNewVideosLive;
    }

    MutableLiveData<List<Video>> getFavoritesItemsLive() {
        return mFavoritesItemsLive;
    }

    public List<Video> getFavoritesVideos() {
        return mFavoritesVideos;
    }

    List<Video> getNewVideos() {
        return mNewVideos;
    }


    List<PlayList> getAllPlayLists() {
        return mAllPlayLists;
    }

    void setAllPlayLists(List<PlayList> mAllPlayLists) {
        this.mAllPlayLists = mAllPlayLists;
    }

    MutableLiveData<List<Video>> getAllFavoritesItemsLive() {
        return mFavoritesItemsLive;
    }

    void setFavoritesItems(List<Video> mFavoritesVideos) {
        this.mFavoritesVideos = mFavoritesVideos;
    }

    boolean removeFavoriteItems(List<Video> itemsToDelete) {
        int deletedItemsCount = 0;
        for (Video video : itemsToDelete) {
            int count = mDb.videoDao().deleteVideoByItemId(video.getId().getVideoId());
            if (count > 0) {
                deletedItemsCount++;
            }
        }
        return deletedItemsCount == itemsToDelete.size();
    }

    void setNewVideos(List<Video> mNewVideos) {
        this.mNewVideos = mNewVideos;
    }

    void setCurrentItem(Video mCurrentVideo) {
        this.mCurrentVideo = mCurrentVideo;
    }

    SingleEventMutableLive<List<PlayListItem>> getPlayListItemsLive() {
        return mPlayListItemsLive;
    }

    String getLastPlayListId() {
        return lastPlayListId;
    }

    SingleEventMutableLive<String> getCurrentItemDescriptionLive() {
        return mCurrentItemDescriptionLive;
    }

    List<PlayListItem> getPlayListItems() {
        return mPlayListItems;
    }
}
