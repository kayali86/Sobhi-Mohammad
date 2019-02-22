package com.kayali_developer.sobhimohammad.utilities;

public class MyTextUtils {

    public static String formatViewsCount(String viewsCount){
        if (viewsCount.toCharArray().length < 7){
            viewsCount = viewsCount.substring(0, viewsCount.toCharArray().length - 3) + "K";
        }else if (viewsCount.toCharArray().length >= 7){
            viewsCount = viewsCount.substring(0, viewsCount.toCharArray().length - 6) + "M";
        }
        return viewsCount;
    }
}
