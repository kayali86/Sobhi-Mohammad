package com.kayali_developer.sobhimohammad.mainactivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kayali_developer.sobhimohammad.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomePageFragment extends Fragment {

    public static final String TAG = "HomePageFragmentTag";

    @BindView(R.id.web_view)
    WebView webView;

    private Unbinder unbinder;
    private HomePageFragmentListener fragmentListener;

    interface HomePageFragmentListener {
        void onHomePageFragmentAttached();
        void onHomePageFragmentDetached();
    }

    public HomePageFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_page, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        webView.loadUrl("https://www.sobhimohammad.com/");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new webViewClient());
        return rootView;
    }

    private class webViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(request.getUrl().toString()));
            if (getActivity() != null && i.resolveActivity(getActivity().getPackageManager()) != null){
                startActivity(i);
                return true;
            }else{
                return false;
            }
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
        fragmentListener = ((HomePageFragmentListener) context);
        fragmentListener.onHomePageFragmentAttached();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentListener.onHomePageFragmentDetached();
    }
}
