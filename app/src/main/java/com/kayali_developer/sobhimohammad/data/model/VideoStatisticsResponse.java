package com.kayali_developer.sobhimohammad.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoStatisticsResponse {

    @SerializedName("kind")
    private String kind;
    @SerializedName("etag")
    private String etag;
    @SerializedName("pageInfo")
    private PageInfoBean pageInfo;
    @SerializedName("items")
    private List<ItemsBean> items;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public PageInfoBean getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfoBean pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class PageInfoBean {

        @SerializedName("totalResults")
        private int totalResults;
        @SerializedName("resultsPerPage")
        private int resultsPerPage;

        public int getTotalResults() {
            return totalResults;
        }

        public void setTotalResults(int totalResults) {
            this.totalResults = totalResults;
        }

        public int getResultsPerPage() {
            return resultsPerPage;
        }

        public void setResultsPerPage(int resultsPerPage) {
            this.resultsPerPage = resultsPerPage;
        }
    }

    public static class ItemsBean {
        @SerializedName("kind")
        private String kind;
        @SerializedName("etag")
        private String etag;
        @SerializedName("id")
        private String id;
        @SerializedName("statistics")
        private StatisticsBean statistics;

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public String getEtag() {
            return etag;
        }

        public void setEtag(String etag) {
            this.etag = etag;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public StatisticsBean getStatistics() {
            return statistics;
        }

        public void setStatistics(StatisticsBean statistics) {
            this.statistics = statistics;
        }

        public static class StatisticsBean {

            @SerializedName("viewCount")
            private String viewCount;
            @SerializedName("likeCount")
            private String likeCount;
            @SerializedName("dislikeCount")
            private String dislikeCount;
            @SerializedName("favoriteCount")
            private String favoriteCount;
            @SerializedName("commentCount")
            private String commentCount;

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

            public String getFavoriteCount() {
                return favoriteCount;
            }

            public void setFavoriteCount(String favoriteCount) {
                this.favoriteCount = favoriteCount;
            }

            public String getCommentCount() {
                return commentCount;
            }

            public void setCommentCount(String commentCount) {
                this.commentCount = commentCount;
            }
        }
    }
}
