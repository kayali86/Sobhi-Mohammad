package com.kayali_developer.sobhimohammad.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Prefs {
    public static void saveNewVideosCount(Context context, int newVideosCount) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("newVideosCount", newVideosCount);
        editor.apply();
    }

    public static int getNewVideosCount(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt("newVideosCount", 50);
    }

    public static void addToViewedItems(Context context, String viewedItemId) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
            List<String> viewedItemsIds = getViewedItemsAsStringArrayListJson(context);
            if (viewedItemsIds == null){
                viewedItemsIds = new ArrayList<>();
            }
            viewedItemsIds.add(viewedItemId);
            editor.putString("viewedItemsIds", new Gson().toJson(viewedItemsIds));
            editor.apply();
    }

    public static List<String> getViewedItemsAsStringArrayListJson(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Type listType = new TypeToken<List<String>>() {}.getType();
        List<String> viewedItemsIds = null;
        try {
            viewedItemsIds = new Gson().fromJson(sharedPreferences.getString("viewedItemsIds", null), listType);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return viewedItemsIds;
    }

}
