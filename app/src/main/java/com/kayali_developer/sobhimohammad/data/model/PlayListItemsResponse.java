package com.kayali_developer.sobhimohammad.data.model;

import com.google.gson.annotations.*;

import java.util.List;

public class PlayListItemsResponse {

    @SerializedName("nextPageToken")
    private String nextPageToken;
    @SerializedName("items")
    private List<PlayListItem> items;

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public List<PlayListItem> getItems() {
        return items;
    }

    public void setItems(List<PlayListItem> items) {
        this.items = items;
    }
}
