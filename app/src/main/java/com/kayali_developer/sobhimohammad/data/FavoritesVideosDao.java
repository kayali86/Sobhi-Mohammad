package com.kayali_developer.sobhimohammad.data;


import com.kayali_developer.sobhimohammad.data.model.PlayListItemsResponse;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface FavoritesVideosDao {
    @Query("SELECT * FROM videos")
    List<PlayListItemsResponse.Item> loadAllFavoriteVideos();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertVideo(PlayListItemsResponse.Item item);

    @Query("DELETE FROM videos WHERE id = :itemId")
    int deleteVideoByItemId(String itemId);

    @Query("DELETE FROM videos")
    int deleteAllFavoriteVideos();

    @Query("SELECT CASE WHEN EXISTS (SELECT * FROM videos WHERE id = :itemId) THEN CAST(1 AS BIT) ELSE CAST(0 AS BIT) END")
    Boolean isFavorite(String itemId);

}
