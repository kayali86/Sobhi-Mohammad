package com.kayali_developer.sobhimohammad.main;

import android.os.Bundle;

import com.kayali_developer.sobhimohammad.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class AboutUsActivity extends AppCompatActivity {
    private AboutUsFragment aboutUsFragment;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        if (savedInstanceState == null){
            fragmentManager = getSupportFragmentManager();
            aboutUsFragment = new AboutUsFragment();
            fragmentManager.beginTransaction().add(R.id.about_us_fragment_container, aboutUsFragment, AboutUsFragment.TAG).commitAllowingStateLoss();
        }

    }
}
