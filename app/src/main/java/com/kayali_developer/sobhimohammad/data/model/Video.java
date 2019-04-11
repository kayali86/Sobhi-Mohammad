package com.kayali_developer.sobhimohammad.data.model;


import com.google.gson.annotations.*;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "videos")
public class Video {
    @NonNull
    @PrimaryKey
    @SerializedName("localVideoId")
    private String localVideoId;
    @SerializedName("id")
    private Id id;
    @SerializedName("snippet")
    private Snippet snippet;
    @SerializedName("statistics")
    private Statistics statistics;
    @SerializedName("selected")
    private boolean selected;

    public String getLocalVideoId() {
        return localVideoId;
    }

    @NonNull
    public void setLocalVideoId(String videoId) {
        this.localVideoId = videoId;
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public Snippet getSnippet() {
        return snippet;
    }

    public void setSnippet(Snippet snippet) {
        this.snippet = snippet;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public static class Id {
        @SerializedName("playlistId")
        private String playlistId;
        @SerializedName("videoId")
        private String videoId;

        public Id(String playlistId, String videoId) {
            this.playlistId = playlistId;
            this.videoId = videoId;
        }

        public String getPlaylistId() {
            return playlistId;
        }

        public void setPlaylistId(String playlistId) {
            this.playlistId = playlistId;
        }

        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }
    }

    public static class Snippet {

        @SerializedName("publishedAt")
        private String publishedAt;
        @SerializedName("title")
        private String title;
        @SerializedName("description")
        private String description;
        @SerializedName("thumbnails")
        private Thumbnails thumbnails;

        public Snippet(String publishedAt, String title, String description, Thumbnails thumbnails) {
            this.publishedAt = publishedAt;
            this.title = title;
            this.description = description;
            this.thumbnails = thumbnails;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Thumbnails getThumbnails() {
            return thumbnails;
        }

        public void setThumbnails(Thumbnails thumbnails) {
            this.thumbnails = thumbnails;
        }

    }
}
