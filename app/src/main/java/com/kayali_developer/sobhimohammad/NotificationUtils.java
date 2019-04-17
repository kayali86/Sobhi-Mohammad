package com.kayali_developer.sobhimohammad;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import com.kayali_developer.sobhimohammad.mainactivity.MainActivity;
import com.kayali_developer.sobhimohammad.utilities.AppDateUtils;

import androidx.core.app.NotificationCompat;

class NotificationUtils {
    private static final String NOTIFICATION_CHANNEL_ID_STR = "Sobhi Mohammad";
    private static final int NOTIFICATION_CHANNEL_ID_INT = 35392;

    static void showNewVideoNotification(Context context, String video_id, String title, String author, String published, String updated, Bitmap largeIcon) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID_STR);

        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.bigPicture(largeIcon);
        bigPictureStyle.setBigContentTitle(author);

        builder.setStyle(bigPictureStyle);
        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(largeIcon);
        builder.setPriority(Notification.PRIORITY_MAX);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setColor(context.getResources().getColor(R.color.colorAccent));
        builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID_STR, NOTIFICATION_CHANNEL_ID_STR, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription(NOTIFICATION_CHANNEL_ID_STR);
            Uri uri= Settings.System.DEFAULT_NOTIFICATION_URI;
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();
            notificationChannel.setSound(uri, audioAttributes);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(notificationChannel);
        } else {
            builder.setPriority(Notification.PRIORITY_MAX);
        }


        Intent notificationIntent = new Intent(context, MainActivity.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent intent = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);

        builder.setContentIntent(intent);

        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(NOTIFICATION_CHANNEL_ID_INT, notification);
    }
}
