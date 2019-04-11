package com.kayali_developer.sobhimohammad.data.model;

import com.google.gson.annotations.SerializedName;

public class Statistics {
    @SerializedName("viewCount")
    private String viewCount;
    @SerializedName("likeCount")
    private String likeCount;
    @SerializedName("dislikeCount")
    private String dislikeCount;

    public Statistics(String viewCount, String likeCount, String dislikeCount) {
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
    }

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getDislikeCount() {
        return dislikeCount;
    }

    public void setDislikeCount(String dislikeCount) {
        this.dislikeCount = dislikeCount;
    }
}
