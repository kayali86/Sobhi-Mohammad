package com.kayali_developer.sobhimohammad.utilities;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.kayali_developer.sobhimohammad.R;
import com.kayali_developer.sobhimohammad.mainactivity.Themes;

import androidx.core.content.ContextCompat;

public class ThemeUtils {

    public static int getThemePrimaryColor(Context context) {

        Themes theme = Prefs.getCurrentTheme(context);

        switch (theme) {

            case RED:
                return context.getResources().getColor(R.color.redColorPrimary);

            case GREEN:
                return context.getResources().getColor(R.color.greenColorPrimary);

            case BLUE:
                return context.getResources().getColor(R.color.blueColorPrimary);

            case BROWN:
                return context.getResources().getColor(R.color.brownColorPrimary);

            case YELLOW:
                return context.getResources().getColor(R.color.yellowColorPrimary);

            default:
                return context.getResources().getColor(R.color.colorPrimary);

        }
    }

    public static int getThemePrimaryColorDark(Context context) {

        Themes theme = Prefs.getCurrentTheme(context);

        switch (theme) {

            case RED:
                return context.getResources().getColor(R.color.redColorPrimaryDark);

            case GREEN:
                return context.getResources().getColor(R.color.greenColorPrimaryDark);

            case BLUE:
                return context.getResources().getColor(R.color.blueColorPrimaryDark);

            case BROWN:
                return context.getResources().getColor(R.color.brownColorPrimaryDark);

            case YELLOW:
                return context.getResources().getColor(R.color.yellowColorPrimaryDark);

            default:
                return context.getResources().getColor(R.color.colorPrimaryDark);

        }
    }

    public static Drawable getThemeGradient(Context context) {

        Themes theme = Prefs.getCurrentTheme(context);

        switch (theme) {

            case RED:
                return ContextCompat.getDrawable(context, R.drawable.gradient_red);

            case GREEN:
                return ContextCompat.getDrawable(context, R.drawable.gradient_green);

            case BLUE:
                return ContextCompat.getDrawable(context, R.drawable.gradient_blue);

            case BROWN:
                return ContextCompat.getDrawable(context, R.drawable.gradient_brown);

            case YELLOW:
                return ContextCompat.getDrawable(context, R.drawable.gradient_yellow);

            default:
                return ContextCompat.getDrawable(context, R.drawable.gradient);

        }
    }


    public static Drawable getThemeGradientCyrcle(Context context) {

        Themes theme = Prefs.getCurrentTheme(context);

        switch (theme) {

            case RED:
                return ContextCompat.getDrawable(context, R.drawable.cyrcle_gradient_red);

            case GREEN:
                return ContextCompat.getDrawable(context, R.drawable.cyrcle_gradient_green);

            case BLUE:
                return ContextCompat.getDrawable(context, R.drawable.cyrcle_gradient_blue);

            case BROWN:
                return ContextCompat.getDrawable(context, R.drawable.cyrcle_gradient_brown);

            case YELLOW:
                return ContextCompat.getDrawable(context, R.drawable.cyrcle_gradient_yellow);

            default:
                return ContextCompat.getDrawable(context, R.drawable.cyrcle_gradient);

        }
    }
}
