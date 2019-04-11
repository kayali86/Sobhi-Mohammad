package com.kayali_developer.sobhimohammad.data.model;

import com.google.gson.annotations.*;

import java.util.List;

public class VideoStatisticsResponse {

    @SerializedName("items")
    private List<Items> items;

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }

    public static class Items {

        @SerializedName("statistics")
        private Statistics statistics;

        public Statistics getStatistics() {
            return statistics;
        }

        public void setStatistics(Statistics statistics) {
            this.statistics = statistics;
        }
    }
}
