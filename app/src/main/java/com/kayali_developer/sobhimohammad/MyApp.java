package com.kayali_developer.sobhimohammad;

import android.app.Activity;
import android.app.Application;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.kayali_developer.sobhimohammad.aboutus.AboutUsActivity;
import com.kayali_developer.sobhimohammad.mainactivity.MainActivity;
import com.kayali_developer.sobhimohammad.settings.SettingsActivity;
import com.kayali_developer.sobhimohammad.videoactivity.VideoActivity;
import com.kayali_developer.sobhimohammad.utilities.ThemeUtils;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                checkTheme(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    private void checkTheme(Activity activity) {
        if (activity instanceof MainActivity){
            int primaryColor = ThemeUtils.getThemePrimaryColor(activity);
            int primaryColorDark = ThemeUtils.getThemePrimaryColorDark(activity);

            MainActivity mainActivity = ((MainActivity) activity);
            mainActivity.mNavigationView.getHeaderView(0).setBackground(ThemeUtils.getThemeGradientCyrcle(activity));
            mainActivity.mNavigationView.setBackground(ThemeUtils.getThemeGradient(activity));

            if (mainActivity.getSupportActionBar() != null){
                mainActivity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(primaryColor));
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = mainActivity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(primaryColorDark);
            }


            // FOR NAVIGATION VIEW ITEM TEXT COLOR
            int[][] state = new int[][] {
                    new int[] {-android.R.attr.state_enabled}, // disabled
                    new int[] {android.R.attr.state_enabled}, // enabled
                    new int[] {-android.R.attr.state_checked}, // unchecked
                    new int[] { android.R.attr.state_pressed}  // pressed

            };

            int[] color = new int[] {
                    primaryColor,
                    primaryColor,
                    primaryColor,
                    Color.YELLOW
            };

            ColorStateList csl = new ColorStateList(state, color);


            // FOR NAVIGATION VIEW ITEM ICON COLOR
            int[][] states = new int[][] {
                    new int[] {-android.R.attr.state_enabled}, // disabled
                    new int[] {android.R.attr.state_enabled}, // enabled
                    new int[] {-android.R.attr.state_checked}, // unchecked
                    new int[] { android.R.attr.state_pressed}  // pressed

            };

            int[] colors = new int[] {
                    primaryColorDark,
                    primaryColorDark,
                    primaryColorDark,
                    Color.YELLOW
            };

            ColorStateList csl2 = new ColorStateList(states, colors);

            mainActivity.mNavigationView.setItemTextColor(csl);
            mainActivity.mNavigationView.setItemIconTintList(csl2);

            //mainActivity.mNavigationView.setItemBackgroundResource(R.drawable.gradient_red);
        }
        else if (activity instanceof AboutUsActivity){
            AboutUsActivity aboutUsActivity = ((AboutUsActivity) activity);
            if (aboutUsActivity.getSupportActionBar() != null){
                aboutUsActivity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ThemeUtils.getThemePrimaryColor(aboutUsActivity)));
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = aboutUsActivity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ThemeUtils.getThemePrimaryColorDark(aboutUsActivity));
            }
        }
        else if (activity instanceof VideoActivity){
            VideoActivity videoActivity = ((VideoActivity) activity);
            if (videoActivity.getSupportActionBar() != null){
                videoActivity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ThemeUtils.getThemePrimaryColor(videoActivity)));
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = videoActivity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ThemeUtils.getThemePrimaryColorDark(videoActivity));
            }
        }
        else if (activity instanceof SettingsActivity){
            SettingsActivity settingsActivity = ((SettingsActivity) activity);
            if (settingsActivity.getSupportActionBar() != null){
                settingsActivity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ThemeUtils.getThemePrimaryColor(settingsActivity)));
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = settingsActivity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ThemeUtils.getThemePrimaryColorDark(settingsActivity));
            }
        }
    }
}
