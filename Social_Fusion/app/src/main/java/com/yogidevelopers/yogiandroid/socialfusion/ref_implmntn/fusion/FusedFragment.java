package com.yogidevelopers.yogiandroid.socialfusion.ref_implmntn.fusion;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Session;
import com.yogidevelopers.yogiandroid.socialfusion.R;
import com.yogidevelopers.yogiandroid.socialfusion.ver_10_22.fusn.recyclerViewFusn.FusionObject;
import com.yogidevelopers.yogiandroid.socialfusion.ver_10_22.fusn.recyclerViewFusn.RecyclerAdapterFS;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FusedFragment extends Fragment {

    private RecyclerView fusionRecyclerView;
    private TextView emptyView;

    private RecyclerAdapterFS recyclerAdapterFS;
    private ArrayList<FusionObject> fusionFeed = new ArrayList<>();

    public FusedFragment() {
        // Required empty public constructor
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        outState.putParcelableArrayList("NewsFeed", fusionFeed);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fused, container, false);

        fusionRecyclerView = (RecyclerView) v.findViewById(R.id.fusionRecyclerFeed);
        emptyView = (TextView) v.findViewById(R.id.emptyView);

        fusionRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerAdapterFS = new RecyclerAdapterFS(getActivity(), new ArrayList<FusionObject>());
        fusionRecyclerView.setAdapter(recyclerAdapterFS);


//        if (savedInstanceState == null || !savedInstanceState.containsKey("NewsFeed")) {
//            Log.e("SavedInstanceState", " Is Empty!");
//        } else {
//            Log.e("SavedInstanceState", " Success!");
//            fusionFeed = savedInstanceState.getParcelableArrayList("NewsFeed");
//        }

        setupNewsFeed();

        return v;
    }

    private void setupNewsFeed() {

        //Check if logged in
        AccessToken fbLoggedIn = AccessToken.getCurrentAccessToken();                  //Facebook
        Session twitterInSession = Twitter.getSessionManager().getActiveSession();     //Twitter

        if (fbLoggedIn != null && twitterInSession != null) {

            if (fusionFeed.size() == 0) {
                loadFusionView();
                recyclerAdapterFS.loadNews(fusionFeed);
            } else {
                recyclerAdapterFS.loadNews(fusionFeed);
            }
        } else {
            setEmptyView();
        }
    }

    private void setEmptyView() {

        emptyView.setVisibility(View.VISIBLE);
        fusionRecyclerView.setVisibility(View.GONE);
    }

    private void loadFusionView() {

    }
}
