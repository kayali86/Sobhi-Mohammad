package com.kayali_developer.sobhimohammad.data;


import com.google.gson.Gson;
import com.kayali_developer.sobhimohammad.data.model.Statistics;
import com.kayali_developer.sobhimohammad.data.model.Video;

import androidx.room.TypeConverter;


class Converters {


    @TypeConverter
    public static Video.Id stringToId(String item) {
        return new Gson().fromJson(item, Video.Id.class);
    }

    @TypeConverter
    public static String idToString(Video.Id item) {
        return new Gson().toJson(item);
    }

    @TypeConverter
    public static Video.Snippet stringToSnippet(String item) {
        return new Gson().fromJson(item, Video.Snippet.class);
    }

    @TypeConverter
    public static String snippetToString(Video.Snippet item) {
        return new Gson().toJson(item);
    }

    @TypeConverter
    public static Statistics stringToStatistics(String item) {
        return new Gson().fromJson(item, Statistics.class);
    }

    @TypeConverter
    public static String statisticsToString(Statistics  item) {
        return new Gson().toJson(item);
    }

}