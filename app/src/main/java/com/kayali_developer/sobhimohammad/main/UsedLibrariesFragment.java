package com.kayali_developer.sobhimohammad.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.kayali_developer.sobhimohammad.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class UsedLibrariesFragment extends Fragment {
    public static final String TAG = "UsedLibrariesFragmentTag";
    @BindView(R.id.lv_libraries)
    ListView lvLibraries;

    private Unbinder unbinder;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_used_libraries, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        if (getContext() != null) {
            ArrayList<String> libraries = new ArrayList<>();
            libraries.add("Retrofit");
            libraries.add("Butter Knife");
            libraries.add("Timber");
            libraries.add("Picasso");
            libraries.add("picasso Transformations");
            libraries.add("Gson");
            libraries.add("YouTube Android Player Api");
            libraries.add("Live Data");
            libraries.add("View Model");
            libraries.add("Lifecycle");
            libraries.add("Firebase");
            libraries.add("Firebase Messaging");
            libraries.add("Expandable Text View");
            libraries.add("Jakewharton Process Phoenix");
            libraries.add("Email Intent Builder");
            LibrariesAdapter librariesAdapter = new LibrariesAdapter(getContext(), libraries);
            lvLibraries.setAdapter(librariesAdapter);
        }


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
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class LibrariesAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<String> libraries;

        public LibrariesAdapter(Context mContext, ArrayList<String> libraries) {
            this.mContext = mContext;
            this.libraries = libraries;
        }

        @Override
        public int getCount() {
            if (libraries == null || libraries.size() == 0) return 0;
            return libraries.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View listItemView = LayoutInflater.from(mContext).inflate(R.layout.item_library, parent, false);
            TextView tv_library = listItemView.findViewById(R.id.tv_library);
            if (libraries.get(position) != null) {
                tv_library.setText(libraries.get(position));
            }
            return listItemView;
        }
    }

}
