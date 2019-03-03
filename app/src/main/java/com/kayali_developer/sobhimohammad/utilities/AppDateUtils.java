package com.kayali_developer.sobhimohammad.utilities;

import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AppDateUtils {
    private static final String YOUTUBE_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:sss";
    private static final String DE_DATE_FORMAT = "dd.MM.yyyy";
    private static final String DE_TIME_DATE_FORMAT = "dd.MM.yyyy HH:mm";

    public static String youtubeFormatToDeFormat(String date) {
        if (date != null){
            Date dateObject = youtubeFormatToDate(date);
            return dateToDeFormat(dateObject);
        }else {
            return null;
        }
    }

    private static Date youtubeFormatToDate(String date) {
        if (date != null){
            date = new StringBuilder().append("\"").append(date).append("\"").toString();
            return new GsonBuilder().setDateFormat(YOUTUBE_DATE_FORMAT).create().fromJson(date, Date.class);
        }else {
            return null;
        }
    }

    private static String dateToDeFormat(Date date) {
        if (date != null){
            String dateStr = new GsonBuilder().setDateFormat(DE_TIME_DATE_FORMAT).create().toJson(date);
            return dateStr.substring(1, dateStr.length() -1);
        }else{
            return null;
        }

    }

    public static long youtubeFormatToMillis(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YOUTUBE_DATE_FORMAT);
        long timeInMilliseconds = -1;
        try {
            Date mDate = simpleDateFormat.parse(date);
            timeInMilliseconds = mDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeInMilliseconds;
    }
}
