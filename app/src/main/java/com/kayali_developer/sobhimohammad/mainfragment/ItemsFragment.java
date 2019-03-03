package com.kayali_developer.sobhimohammad.mainfragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.kayali_developer.sobhimohammad.R;
import com.kayali_developer.sobhimohammad.data.model.PlayListItemsResponse;
import com.kayali_developer.sobhimohammad.mainactivity.MainActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ItemsFragment extends Fragment implements ItemsAdapter.ItemsAdapterListener {

    public static final String PLAY_LIST_ITEMS_FRAGMENT_TAG = "PLAY_LIST_ITEMS_FRAGMENT_TAG";
    public static final String PLAY_LIST_ITEMS_RESPONSE_KEY = "play_list_items_response_key";
    public static final String PLAY_LIST_TITLE_KEY = "play_list_title_key";

    @BindView(R.id.rv_play_list_items)
    RecyclerView rvPlayListItems;
    @BindView(R.id.no_items_view)
    LinearLayout noItemsView;
    private Unbinder unbinder;
    private MainActivity mActivity;

    public ItemsFragment() {
    }

    private ItemsFragmentListener mItemsFragmentListener;

    public interface ItemsFragmentListener {
        void onPlayListItemClicked(PlayListItemsResponse.Item item);

        void onItemsFragmentAttached();

        void onItemsFragmentDetached();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_items, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        if (getArguments() != null) {
            String mTitle = getArguments().getString(PLAY_LIST_TITLE_KEY);
            if (mTitle != null && mActivity != null){
                mActivity.setTitle(mTitle);
            }
            List<PlayListItemsResponse.Item> items;

            String responseStr = getArguments().getString(PLAY_LIST_ITEMS_RESPONSE_KEY);
            if (responseStr != null) {
                TypeToken<List<PlayListItemsResponse.Item>> token = new TypeToken<List<PlayListItemsResponse.Item>>() {};
                try {
                    items = new Gson().fromJson(responseStr, token.getType());
                    populatePlayLists(items);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }catch (NullPointerException npe){
                    npe.printStackTrace();
                }
            }

        }
        return rootView;
    }

    private void populatePlayLists(List<PlayListItemsResponse.Item> items) {
        if (items != null && items.size() > 0) {
            ItemsAdapter mAdapter = new ItemsAdapter(this::onPlayListItemClicked);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            rvPlayListItems.setLayoutManager(layoutManager);
            rvPlayListItems.hasFixedSize();
            rvPlayListItems.setAdapter(mAdapter);
            mAdapter.setData(items);
        } else {
            noItemsView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPlayListItemClicked(PlayListItemsResponse.Item item) {
        mItemsFragmentListener.onPlayListItemClicked(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mItemsFragmentListener = (ItemsFragmentListener) context;
        mItemsFragmentListener.onItemsFragmentAttached();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mItemsFragmentListener.onItemsFragmentDetached();
    }
}
