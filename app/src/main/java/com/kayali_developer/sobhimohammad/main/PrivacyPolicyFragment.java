package com.kayali_developer.sobhimohammad.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kayali_developer.sobhimohammad.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PrivacyPolicyFragment extends Fragment {
    public static final String TAG = "PrivacyPolicyFragmentTag";

    private Unbinder unbinder;

    private PrivacyPolicyFragmentListener fragmentListener;

    interface PrivacyPolicyFragmentListener{
        void onPrivacyPolicyFragmentAttached();
        void onPrivacyPolicyFragmentDetached();
    }

    public PrivacyPolicyFragment() {
    }

    public static PrivacyPolicyFragment newInstance() {
        return new PrivacyPolicyFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_privacy_policy, container, false);
        unbinder = ButterKnife.bind(this, rootView);


        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentListener = ((PrivacyPolicyFragmentListener) context);
        fragmentListener.onPrivacyPolicyFragmentAttached();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentListener.onPrivacyPolicyFragmentDetached();
    }
}
