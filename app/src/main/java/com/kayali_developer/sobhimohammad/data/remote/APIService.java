package com.kayali_developer.sobhimohammad.data.remote;


import com.kayali_developer.sobhimohammad.data.model.PlayListItemsResponse;
import com.kayali_developer.sobhimohammad.data.model.PlayListsResponse;
import com.kayali_developer.sobhimohammad.data.model.VideoStatisticsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {
    @GET("youtube/v3/playlists?")
    Call<PlayListsResponse> getPlaylists
            (@Query("part") String part,
             @Query("channelId") String channelId,
             @Query("maxResults") int maxResults,
             @Query("key") String apiKey);

    @GET("youtube/v3/playlistItems?")
    Call<PlayListItemsResponse> getPlaylistItems
            (@Query("part") String part,
             @Query("playlistId") String playlistId,
             @Query("maxResults") int maxResults,
             @Query("order") String order,
             @Query("key") String apiKey);

    @GET("youtube/v3/videos?")
    Call<VideoStatisticsResponse> getVideoStatistics
            (@Query("part") String part,
             @Query("id") String videoId,
             @Query("key") String apiKey);
}