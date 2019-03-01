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

import com.kayali_developer.sobhimohammad.main.MainActivity;
import com.kayali_developer.sobhimohammad.main.Themes;
import com.kayali_developer.sobhimohammad.utilities.Prefs;

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
            int primaryColor = getResources().getColor(R.color.colorPrimary);
            int primaryColorDark = getResources().getColor(R.color.colorPrimaryDark);
            int cycle_gradient_id = R.drawable.cyrcle_gradient;
            int gradient_id = R.drawable.gradient;

            Themes theme = Prefs.getCurrentTheme(activity);

            switch (theme){

                case RED:
                    primaryColor = getResources().getColor(R.color.redColorPrimary);
                    primaryColorDark = getResources().getColor(R.color.redColorPrimaryDark);
                    cycle_gradient_id = R.drawable.cyrcle_gradient_red;
                    gradient_id = R.drawable.gradient_red;
                    break;

                case GREEN:
                    primaryColor = getResources().getColor(R.color.greenColorPrimary);
                    primaryColorDark = getResources().getColor(R.color.greenColorPrimaryDark);
                    cycle_gradient_id = R.drawable.cyrcle_gradient_green;
                    gradient_id = R.drawable.gradient_green;
                    break;

                case BLUE:
                    primaryColor = getResources().getColor(R.color.blueColorPrimary);
                    primaryColorDark = getResources().getColor(R.color.blueColorPrimaryDark);
                    cycle_gradient_id = R.drawable.cyrcle_gradient_blue;
                    gradient_id = R.drawable.gradient_blue;
                    break;

                case BROWN:
                    primaryColor = getResources().getColor(R.color.brownColorPrimary);
                    primaryColorDark = getResources().getColor(R.color.brownColorPrimaryDark);
                    cycle_gradient_id = R.drawable.cyrcle_gradient_brown;
                    gradient_id = R.drawable.gradient_brown;
                    break;

                case PURPLE:
                    primaryColor = getResources().getColor(R.color.colorPrimary);
                    primaryColorDark = getResources().getColor(R.color.colorPrimaryDark);
                    cycle_gradient_id = R.drawable.cyrcle_gradient;
                    gradient_id = R.drawable.gradient;
                    break;

                case YELLOW:
                    primaryColor = getResources().getColor(R.color.yellowColorPrimary);
                    primaryColorDark = getResources().getColor(R.color.yellowColorPrimaryDark);
                    cycle_gradient_id = R.drawable.cyrcle_gradient_yellow;
                    gradient_id = R.drawable.gradient_yellow;
                    break;

            }
            MainActivity mainActivity = ((MainActivity) activity);
            mainActivity.mNavigationView.getHeaderView(0).setBackgroundResource(cycle_gradient_id);
            mainActivity.mNavigationView.setBackgroundResource(gradient_id);

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
    }
}
