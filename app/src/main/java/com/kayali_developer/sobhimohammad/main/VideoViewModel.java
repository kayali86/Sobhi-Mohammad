package com.kayali_developer.sobhimohammad.main;

import android.app.Application;

import com.kayali_developer.sobhimohammad.data.AppDatabase;
import com.kayali_developer.sobhimohammad.data.model.PlayListItemsResponse;
import com.kayali_developer.sobhimohammad.data.model.VideoStatisticsResponse;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class VideoViewModel extends AndroidViewModel {

    private PlayListItemsResponse.Item mCurrentItem = null;
    private VideoStatisticsResponse mItemStatistics = null;

    private AppDatabase mDb;

    public VideoViewModel(@NonNull Application application) {
        super(application);
        mDb = AppDatabase.getInstance(application);
    }

    boolean isFavorite(String itemId) {
        return mDb.videoDao().isFavorite(itemId);
    }

    boolean addToFavorites(PlayListItemsResponse.Item currentItem) {
        long addedVideosCount = mDb.videoDao().insertVideo(currentItem);
        return addedVideosCount > 0;
    }

    boolean removeFromFavorites(String itemId) {
        long removedVideosCount = mDb.videoDao().deleteVideoByItemId(itemId);
        return removedVideosCount > 0;
    }

    public PlayListItemsResponse.Item getCurrentItem() {
        return mCurrentItem;
    }

    public void setCurrentItem(PlayListItemsResponse.Item mCurrentItem) {
        this.mCurrentItem = mCurrentItem;
    }

    public VideoStatisticsResponse getItemStatistics() {
        return mItemStatistics;
    }

    public void setItemStatistics(VideoStatisticsResponse mItemStatistics) {
        this.mItemStatistics = mItemStatistics;
    }


}
