package com.kayali_developer.sobhimohammad.mainactivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.processphoenix.ProcessPhoenix;
import com.kayali_developer.sobhimohammad.R;
import com.kayali_developer.sobhimohammad.utilities.Prefs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ChangeThemeFragment extends Fragment {

    public static final String TAG = "ChangeThemeFragmentTag";

    @BindView(R.id.purple_theme)
    View purpleTheme;
    @BindView(R.id.red_theme)
    View redTheme;
    @BindView(R.id.yellow_theme)
    View yellowTheme;
    @BindView(R.id.brown_theme)
    View brownTheme;
    @BindView(R.id.green_theme)
    View greenTheme;
    @BindView(R.id.blue_theme)
    View blueTheme;

    private Unbinder unbinder;
    private ChangeThemeFragmentListener fragmentListener;

    interface ChangeThemeFragmentListener {
        void onChangeThemeFragmentAttached();

        void onChangeThemeFragmentDetached();
    }

    public ChangeThemeFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_change_theme, container, false);
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
        fragmentListener = ((ChangeThemeFragmentListener) context);
        fragmentListener.onChangeThemeFragmentAttached();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentListener.onChangeThemeFragmentDetached();
    }


    @OnClick({R.id.purple_theme, R.id.red_theme, R.id.yellow_theme, R.id.brown_theme, R.id.green_theme, R.id.blue_theme})
    void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.purple_theme:
                Prefs.setCurrentTheme(getContext(), Themes.PURPLE);
                purpleTheme.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                break;

            case R.id.red_theme:
                Prefs.setCurrentTheme(getContext(), Themes.RED);
                redTheme.setBackgroundColor(getResources().getColor(R.color.redColorPrimaryDark));
                break;

            case R.id.yellow_theme:
                Prefs.setCurrentTheme(getContext(), Themes.YELLOW);
                yellowTheme.setBackgroundColor(getResources().getColor(R.color.yellowColorPrimaryDark));
                break;

            case R.id.brown_theme:
                Prefs.setCurrentTheme(getContext(), Themes.BROWN);
                brownTheme.setBackgroundColor(getResources().getColor(R.color.brownColorPrimaryDark));
                break;

            case R.id.green_theme:
                Prefs.setCurrentTheme(getContext(), Themes.GREEN);
                greenTheme.setBackgroundColor(getResources().getColor(R.color.greenColorPrimaryDark));
                break;

            case R.id.blue_theme:
                Prefs.setCurrentTheme(getContext(), Themes.BLUE);
                blueTheme.setBackgroundColor(getResources().getColor(R.color.blueColorPrimaryDark));
                break;

        }
        showRestartDialog();
    }

    private void showRestartDialog(){
        DialogInterface.OnClickListener positiveButtonClickListener = (this::onRestartAppButtonClicked);
        showConfirmDialog(positiveButtonClickListener, getString(R.string.restart_app_warning), getString(R.string.restart), getString(R.string.not_now));
    }

    private void onRestartAppButtonClicked(DialogInterface dialogInterface, int i) {
        if (getContext() != null){
            Intent restartIntent = new Intent(getActivity(), MainActivity.class);
            ProcessPhoenix.triggerRebirth(getContext(), restartIntent);
            ProcessPhoenix.triggerRebirth(getContext());
        }
    }

    // Display an alert dialog
    private void showConfirmDialog(DialogInterface.OnClickListener positiveButtonClickListener, String message, String positiveButtonCaption, String negativeButtonCaption) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(message);
        builder.setPositiveButton(positiveButtonCaption, positiveButtonClickListener);
        builder.setNegativeButton(negativeButtonCaption, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
