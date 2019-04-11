package com.kayali_developer.sobhimohammad.settings;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kayali_developer.sobhimohammad.R;
import com.kayali_developer.sobhimohammad.utilities.Prefs;
import com.kayali_developer.sobhimohammad.utilities.ThemeUtils;

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
    @BindView(R.id.tv_new_videos_count_hint)
    TextView tvNewVideosCountHint;
    @BindView(R.id.tv_notification_title)
    TextView tvNotificationTitle;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        mContext = this;

        tvNewVideosCountHint.setTextColor(ThemeUtils.getThemePrimaryColor(this));
        tvNotificationTitle.setTextColor(ThemeUtils.getThemePrimaryColor(this));

        etvNewVideosCount.setTextColor(ThemeUtils.getThemePrimaryColorDark(this));
        etvNewVideosCount.setText(String.valueOf(Prefs.getNewVideosCount(mContext)));

        swDarkMode.setHintTextColor(ThemeUtils.getThemePrimaryColorDark(this));
        swDarkMode.setChecked(Prefs.getDarkModeStatus(mContext));

        swNewVideosNotification.setHintTextColor(ThemeUtils.getThemePrimaryColorDark(this));
        swNewVideosNotification.setChecked(Prefs.getNewVideosNotificationStatus(mContext));

        swGeneralNotification.setHintTextColor(ThemeUtils.getThemePrimaryColorDark(this));
        swGeneralNotification.setChecked(Prefs.getGeneralNotificationStatus(mContext));

        etvNewVideosCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int Count = -1;
                if (s != null && s.length() > 0) {
                    try {
                        count = Integer.valueOf(s.toString());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }

                if (count > 0) {
                    Prefs.saveNewVideosCount(mContext, count);
                    showToastMessage(getString(R.string.saved));
                } else {
                    showToastMessage(getString(R.string.enter_valid_number));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        swDarkMode.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> Prefs.setDarkModeStatus(mContext, isChecked));

        swNewVideosNotification.setOnCheckedChangeListener(this::onNewVideosNotificationSwitchCheckedChanged);

        swGeneralNotification.setOnCheckedChangeListener(this::onGeneralNotificationSwitchCheckedChanged);

    }

    private void onNewVideosNotificationSwitchCheckedChanged(CompoundButton buttonView, boolean isChecked){
        if (isChecked) {
            FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.new_video_topic))
                    .addOnCompleteListener(this::onNewVideosNotificationSubscribed);
        } else {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(getString(R.string.new_video_topic))
                    .addOnCompleteListener(this::onNewVideosNotificationUnsubscribed);
        }
    }

    private void onNewVideosNotificationSubscribed(@NonNull Task<Void> task){
        if (!task.isSuccessful()) {
            showToastMessage(getString(R.string.subscription_error));
        } else {
            Prefs.setNewVideosNotificationStatus(mContext, true);
            showToastMessage(getString(R.string.subscribed));
        }
    }


    private void onNewVideosNotificationUnsubscribed(@NonNull Task<Void> task){
        if (!task.isSuccessful()) {
            showToastMessage(getString(R.string.unsubscription_error));
        } else {
            Prefs.setNewVideosNotificationStatus(mContext, false);
            showToastMessage(getString(R.string.unsubscribed));
        }
    }

    private void onGeneralNotificationSwitchCheckedChanged(CompoundButton buttonView, boolean isChecked){
        if (isChecked) {
            FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.general_topic))
                    .addOnCompleteListener(this::onGeneralNotificationSubscribed);
        } else {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(getString(R.string.general_topic))
                    .addOnCompleteListener(this::onGeneralNotificationUnsubscribed);
        }
    }

    private void onGeneralNotificationSubscribed(@NonNull Task<Void> task){
        if (!task.isSuccessful()) {
            showToastMessage(getString(R.string.subscription_error));
        } else {
            Prefs.setGeneralNotificationStatus(mContext, true);
            showToastMessage(getString(R.string.subscribed));
        }
    }

    private void onGeneralNotificationUnsubscribed(@NonNull Task<Void> task){
        if (!task.isSuccessful()) {
            showToastMessage(getString(R.string.unsubscription_error));
        } else {
            Prefs.setNewVideosNotificationStatus(mContext, false);
            showToastMessage(getString(R.string.unsubscribed));
        }
    }

    private void showToastMessage(String message) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
    }
}
