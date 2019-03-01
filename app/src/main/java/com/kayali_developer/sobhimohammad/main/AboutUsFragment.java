package com.kayali_developer.sobhimohammad.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kayali_developer.sobhimohammad.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AboutUsFragment extends Fragment {
    public static final String TAG = "AboutUsFragmentTag";

    private Unbinder unbinder;
    private AboutUsActivity mActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about_us, container, false);
        unbinder = ButterKnife.bind(this, rootView);


        return rootView;
    }

    private void openWebSite(String url){
        if (getActivity() != null){
            if (!url.startsWith("http://") && !url.startsWith("https://")){
                url = "http://" + url;
            }

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            if (browserIntent.resolveActivity(getActivity().getPackageManager()) != null){
                startActivity(browserIntent);
            }
        }
    }

    @OnClick({R.id.tv_developer_website, R.id.iv_facebook, R.id.iv_facebook2, R.id.iv_youtube, R.id.iv_twitter, R.id.iv_instagram, R.id.iv_google_play})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.tv_developer_website:
                DeveloperFragment developerFragment = new DeveloperFragment();
                mActivity.fragmentManager.beginTransaction().add(R.id.about_us_fragment_container, developerFragment, DeveloperFragment.TAG).addToBackStack(null).commitAllowingStateLoss();
                break;

            case R.id.iv_facebook:
                openWebSite("https://www.facebook.com/sobhi.mhammad");
                break;

            case R.id.iv_facebook2:
                openWebSite("https://www.facebook.com/sobhimoeofficial");
                break;

            case R.id.iv_youtube:
                openWebSite("https://www.youtube.com/channel/UCEiwa5MRvm0mV85wpLTLUBw");
                break;

            case R.id.iv_twitter:
                openWebSite("https://twitter.com/xxv3");
                break;

            case R.id.iv_instagram:
                openWebSite("https://www.instagram.com/sobhi_mhammad/");
                break;

            case R.id.iv_google_play:
                openWebSite("https://play.google.com/store/apps/details?id=com.app.sobhimohammad");
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = ((AboutUsActivity) context);
    }
}
