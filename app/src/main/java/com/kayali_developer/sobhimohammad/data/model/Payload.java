package com.kayali_developer.sobhimohammad.data.model;

import com.google.gson.annotations.*;

public class Payload {
    @SerializedName("author")
    private String author;
    @SerializedName("published")
    private String published;
    @SerializedName("title")
    private String title;
    @SerializedName("channel_id")
    private String channel_id;
    @SerializedName("updated")
    private String updated;
    @SerializedName("video_id")
    private String video_id;

    public String getAuthor() {
        return author;
    }

    public String getPublished() {
        return published;
    }

    public String getTitle() {
        return title;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public String getUpdated() {
        return updated;
    }

    public String getVideo_id() {
        return video_id;
    }
}
