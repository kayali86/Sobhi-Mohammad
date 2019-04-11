package com.kayali_developer.sobhimohammad.data.model;

import com.google.gson.annotations.*;

import java.util.List;

public class PlayListsResponse {
    @SerializedName("nextPageToken")
    private String nextPageToken;
    @SerializedName("items")
    private List<PlayList> items;

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public List<PlayList> getItems() {
        return items;
    }

    public void setItems(List<PlayList> items) {
        this.items = items;
    }

}
