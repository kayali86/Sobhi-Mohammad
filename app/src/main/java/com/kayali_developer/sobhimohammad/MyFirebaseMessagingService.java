package com.kayali_developer.sobhimohammad;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.kayali_developer.sobhimohammad.data.model.PushNotification;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FirebaseMessaging";
    private PushNotification mPushNotification;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Map<String, String> receivedMap = remoteMessage.getData();
            String pushNotificationJson = receivedMap.get("data");
            mPushNotification = new Gson().fromJson(pushNotificationJson, PushNotification.class);


            Log.e(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>Notification Message data getVideo_id: " + mPushNotification.getPayload().getVideo_id());
            Log.e(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>Notification Message data getChannel_id: " + mPushNotification.getPayload().getChannel_id());
            Log.e(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>Notification Message data getTitle: " + mPushNotification.getPayload().getTitle());
            Log.e(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>Notification Message data getAuthor: " + mPushNotification.getPayload().getAuthor());
            Log.e(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>Notification Message data getPublished: " + mPushNotification.getPayload().getPublished());
            Log.e(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>Notification Message data getUpdated: " + mPushNotification.getPayload().getUpdated());

            String largeIconUrl = "https://img.youtube.com/vi/" + mPushNotification.getPayload().getVideo_id() + "/mqdefault.jpg";
            new SendNotification(this).execute(largeIconUrl);
        }


    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>Refreshed token: " + s);
    }


    private class SendNotification extends AsyncTask<String, Void, Bitmap> {

        Context ctx;

        SendNotification(Context context) {
            super();
            this.ctx = context;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            InputStream in;
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                return BitmapFactory.decodeStream(in);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
                NotificationUtils.showNewVideoNotification(MyFirebaseMessagingService.this, mPushNotification.getPayload().getVideo_id(), mPushNotification.getPayload().getTitle(),
                        mPushNotification.getPayload().getAuthor(), mPushNotification.getPayload().getPublished(), mPushNotification.getPayload().getUpdated(), result);
        }
    }
}