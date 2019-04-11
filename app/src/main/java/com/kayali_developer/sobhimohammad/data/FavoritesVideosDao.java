package com.kayali_developer.sobhimohammad.data;


import com.kayali_developer.sobhimohammad.data.model.Video;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface FavoritesVideosDao {
    @Query("SELECT * FROM videos")
    List<Video> loadAllFavoriteVideos();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertVideo(Video video);

    @Query("DELETE FROM videos WHERE localVideoId = :itemId")
    int deleteVideoByItemId(String itemId);

    @Query("SELECT CASE WHEN EXISTS (SELECT * FROM videos WHERE localVideoId = :itemId) THEN CAST(1 AS BIT) ELSE CAST(0 AS BIT) END")
    Boolean isFavorite(String itemId);

}
