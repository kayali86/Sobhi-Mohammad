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

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }
}
