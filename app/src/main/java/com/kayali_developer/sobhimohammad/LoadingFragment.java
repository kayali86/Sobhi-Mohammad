package com.kayali_developer.sobhimohammad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LoadingFragment extends Fragment {
    public static final String TAG = "LoadingFragmentTag";
    public static final String MESSAGE_KEY = "LoadingFragmentMessage";
    @BindView(R.id.tv_loading_message)
    TextView tvLoadingMessage;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_loading, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        if (getArguments() != null && getArguments().getString(MESSAGE_KEY) != null) {
            tvLoadingMessage.setText(getArguments().getString(MESSAGE_KEY));
        }

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
