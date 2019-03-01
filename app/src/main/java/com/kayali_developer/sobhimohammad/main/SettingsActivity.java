package com.kayali_developer.sobhimohammad.main;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kayali_developer.sobhimohammad.R;
import com.kayali_developer.sobhimohammad.utilities.Prefs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.etv_new_videos_count)
    EditText etvNewVideosCount;
    @BindView(R.id.sw_new_videos_notification)
    Switch swNewVideosNotification;
    @BindView(R.id.sw_general_notification)
    Switch swGeneralNotification;
    @BindView(R.id.sw_dark_mode)
    Switch swDarkMode;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        mContext = this;

        etvNewVideosCount.setText(String.valueOf(Prefs.getNewVideosCount(mContext)));
        swDarkMode.setChecked(Prefs.getDarkModeStatus(mContext));
        swNewVideosNotification.setChecked(Prefs.getNewVideosNotificationStatus(mContext));
        swGeneralNotification.setChecked(Prefs.getGeneralNotificationStatus(mContext));

        etvNewVideosCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int Count = -1;
                if (s != null && s.length() > 0){
                    try {
                        count = Integer.valueOf(s.toString());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }

                if (count > 0){
                    Prefs.saveNewVideosCount(mContext, count);
                    showToastMessage("Saved");
                }else{
                    showToastMessage("Please enter a valid number!");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        swDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Prefs.setDarkModeStatus(mContext, isChecked);
            }
        });

        swNewVideosNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.new_video_topic))
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (!task.isSuccessful()) {
                                        showToastMessage("Error");
                                    }else{
                                        Prefs.setNewVideosNotificationStatus(mContext, true);
                                        showToastMessage("Success");
                                    }
                                }
                            });
                }else{
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(getString(R.string.new_video_topic))
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (!task.isSuccessful()) {
                                        showToastMessage("Error");
                                    }else{
                                        Prefs.setNewVideosNotificationStatus(mContext, false);
                                        showToastMessage("Success");
                                    }
                                }
                            });
                }
            }
        });

        swGeneralNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.general_topic))
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (!task.isSuccessful()) {
                                        showToastMessage("Error");
                                    }else{
                                        Prefs.setGeneralNotificationStatus(mContext, true);
                                        showToastMessage("Success");
                                    }
                                }
                            });
                }else{
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(getString(R.string.general_topic))
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (!task.isSuccessful()) {
                                        showToastMessage("Error");
                                    }else{
                                        Prefs.setNewVideosNotificationStatus(mContext, false);
                                        showToastMessage("Success");
                                    }
                                }
                            });
                }
            }
        });
    }


    private void showToastMessage(String message) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
    }
}
