package com.kayali_developer.sobhimohammad.videoactivity;

import android.app.Application;

import com.kayali_developer.sobhimohammad.data.AppDatabase;
import com.kayali_developer.sobhimohammad.data.model.Video;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class VideoViewModel extends AndroidViewModel {

    private Video mCurrentVideo = null;

    private AppDatabase mDb;

    public VideoViewModel(@NonNull Application application) {
        super(application);
        mDb = AppDatabase.getInstance(application);
    }

    boolean isFavorite(String itemId) {
        return mDb.videoDao().isFavorite(itemId);
    }

    boolean addToFavorites(Video currentVideo) {
        currentVideo.setLocalVideoId(currentVideo.getId().getVideoId());
        long addedVideosCount = mDb.videoDao().insertVideo(currentVideo);
        return addedVideosCount > 0;
    }

    boolean removeFromFavorites(String itemId) {
        long removedVideosCount = mDb.videoDao().deleteVideoByItemId(itemId);
        return removedVideosCount > 0;
    }

    Video getCurrentItem() {
        return mCurrentVideo;
    }

    void setCurrentItem(Video mCurrentVideo) {
        this.mCurrentVideo = mCurrentVideo;
    }

}
