package com.kayali_developer.sobhimohammad.data.model;

import com.google.gson.annotations.*;

import java.util.List;

public class getNewVideosResponse {

    @SerializedName("nextPageToken")
    private String nextPageToken;
    @SerializedName("items")
    private List<Video> videos;

    public String getNextPageToken() {
        return nextPageToken;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }
}
