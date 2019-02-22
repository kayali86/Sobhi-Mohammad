package com.kayali_developer.sobhimohammad.data;


import com.google.gson.Gson;
import com.kayali_developer.sobhimohammad.data.model.PlayListItemsResponse;

import androidx.room.TypeConverter;


public class Converters {

    @TypeConverter
    public static PlayListItemsResponse.Item.SnippetBean stringToPlayListItemsResponseItemSnippetBean(String playListItemsResponseItemSnippetBean) {
        return new Gson().fromJson(playListItemsResponseItemSnippetBean, PlayListItemsResponse.Item.SnippetBean.class);
    }

    @TypeConverter
    public static String playListItemsResponseItemSnippetBeanToString(PlayListItemsResponse.Item.SnippetBean playListItemsResponseItemSnippetBean) {
        return new Gson().toJson(playListItemsResponseItemSnippetBean);
    }

}