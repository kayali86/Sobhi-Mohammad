package com.kayali_developer.sobhimohammad.data.model;

import com.google.gson.annotations.SerializedName;

public class PlayList {

    @SerializedName("id")
    private String id;
    @SerializedName("snippet")
    private Snippet snippet;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Snippet getSnippet() {
        return snippet;
    }

    public void setSnippet(Snippet snippet) {
        this.snippet = snippet;
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

        public String getPublishedAt() {
            return publishedAt;
        }


        public String getTitle() {
            return title;
        }


        public String getDescription() {
            return description;
        }


        public Thumbnails getThumbnails() {
            return thumbnails;
        }


        public static class Thumbnails {

            @SerializedName("high")
            private High high;

            public High getHigh() {
                return high;
            }


            public static class High {

                @SerializedName("url")
                private String url;
                @SerializedName("width")
                private int width;
                @SerializedName("height")
                private int height;

                public String getUrl() {
                    return url;
                }

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }
            }
        }
    }
}
