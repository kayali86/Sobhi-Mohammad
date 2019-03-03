package com.kayali_developer.sobhimohammad;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import com.kayali_developer.sobhimohammad.mainactivity.MainActivity;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new CountDownTimer(1500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                startMainActivity();
            }
        }.start();
    }

    private void startMainActivity(){
        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
        if (intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
            finish();
        }
    }

    private void setFullScreenMode() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setFullScreenMode();
    }
}
