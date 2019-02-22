package com.kayali_developer.sobhimohammad;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;

import com.kayali_developer.sobhimohammad.utilities.AppDateUtils;

import androidx.core.app.NotificationCompat;

class NotificationUtils {
    private static final String NOTIFICATION_CHANNEL_ID_STR = "Sobhi Mohammad";
    private static final int NOTIFICATION_CHANNEL_ID_INT = 35392;

    static void showNewVideoNotification(Context context, String video_id, String title, String author, String published, String updated, Bitmap largeIcon) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID_STR);
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle(author);
        bigTextStyle.bigText(title + "\n" + AppDateUtils.youtubeFormatToDeFormat(published));
        builder.setStyle(bigTextStyle);
        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(largeIcon);
        builder.setPriority(Notification.PRIORITY_MAX);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setColor(context.getResources().getColor(R.color.colorAccent));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID_STR, NOTIFICATION_CHANNEL_ID_STR, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription(NOTIFICATION_CHANNEL_ID_STR);
            notificationChannel.setSound(null, null);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(notificationChannel);
        } else {
            builder.setPriority(Notification.PRIORITY_MAX);
        }

        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(NOTIFICATION_CHANNEL_ID_INT, notification);
    }

}
