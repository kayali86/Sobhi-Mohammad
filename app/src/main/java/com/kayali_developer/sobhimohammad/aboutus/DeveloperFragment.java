package com.kayali_developer.sobhimohammad.aboutus;

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
import de.cketti.mailto.EmailIntentBuilder;

public class DeveloperFragment extends Fragment {
    public static final String TAG = "DeveloperFragmentTag";

    private Unbinder unbinder;
    private AboutUsActivity mActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_developer, container, false);
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

    private void openEmail(String address, String subject){
        if (getContext() != null && getActivity() != null){
            Intent emailIntent = EmailIntentBuilder.from(getContext())
                    .to(address)
                    .subject(subject)
                    .build();
            if (emailIntent.resolveActivity(getActivity().getPackageManager()) != null){
                startActivity(emailIntent);
            }
        }
    }

    @OnClick({R.id.tv_developer_website, R.id.tv_developer_email, R.id.tv_used_libraries})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.tv_developer_website:
                openWebSite("http://kayali-developer.com/");
                break;

            case R.id.tv_developer_email:
                openEmail("kayali.developer@gmail.com", "Sobhi Mohammad App");
                break;

            case R.id.tv_used_libraries:
                UsedLibrariesFragment usedLibrariesFragment = new UsedLibrariesFragment();
                mActivity.fragmentManager.beginTransaction().add(R.id.about_us_fragment_container, usedLibrariesFragment, UsedLibrariesFragment.TAG).addToBackStack(null).commitAllowingStateLoss();
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




