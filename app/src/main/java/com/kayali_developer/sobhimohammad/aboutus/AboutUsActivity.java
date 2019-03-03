package com.kayali_developer.sobhimohammad.aboutus;

import android.os.Bundle;
import android.view.MenuItem;

import com.kayali_developer.sobhimohammad.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class AboutUsActivity extends AppCompatActivity {
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null){
            AboutUsFragment aboutUsFragment = new AboutUsFragment();
            fragmentManager.beginTransaction().replace(R.id.about_us_fragment_container, aboutUsFragment, AboutUsFragment.TAG).commitAllowingStateLoss();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return(super.onOptionsItemSelected(item));
    }
}
