package com.yogidevelopers.yogiandroid.socialfusion.ref_implmntn.fusion;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yogidevelopers.yogiandroid.socialfusion.R;
import com.yogidevelopers.yogiandroid.socialfusion.ver_10_22.fusn.recyclerViewFusn.FusionObject;
import com.yogidevelopers.yogiandroid.socialfusion.ver_10_22.fusn.recyclerViewFusn.RecyclerAdapterFS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class FusionFragment extends Fragment {

    private ArrayList<FusionObject> fusionFeed = new ArrayList<>();

    private TextView emptyText;

    private RecyclerView recyclerView;
    private RecyclerAdapterFS fusionAdapter;

    public FusionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fusion, container, false);

        fusionFeed = getArguments().getParcelableArrayList("fuFeed");

        recyclerView = (RecyclerView) v.findViewById(R.id.fusionFeed);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fusionAdapter = new RecyclerAdapterFS(getActivity(), new ArrayList<FusionObject>());
        recyclerView.setAdapter(fusionAdapter);

        emptyText = (TextView) v.findViewById(R.id.emptyText);

        if (fusionFeed.size() != 0) {

            recyclerView.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.GONE);

            processFeed();
        } else {
            recyclerView.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
        }

        return v;
    }

    private void processFeed() {

        Collections.sort(fusionFeed, FusionObject.PostDate);
        Collections.reverse(fusionFeed);

        ArrayList<Integer> removeIndexes = new ArrayList<>();

        Log.e("Fusion fees size", fusionFeed.size()+"");

        for(int i = 0; i<fusionFeed.size(); i++){

            FusionObject ithObj = fusionFeed.get(i);
//            Log.i("For ith", i+" "+ithObj.toString());

            for( int j=i+1; j<fusionFeed.size(); j++){

                FusionObject jthObj = fusionFeed.get(j);

//                Log.i("For Jth", j+" "+jthObj.toString());

                if(ithObj.getHashTags().compareTo(jthObj.getHashTags()) == 0
                        && !ithObj.getObjType().equals(jthObj.getObjType())
                        && !ithObj.getHashTags().equals("")
                        && !jthObj.getHashTags().equals("")
                        && !ithObj.getId().equals(jthObj.getId())){

                    Log.e("IN if", "MATCH FOUND !");

                    Log.i("Ith is", ithObj.toString());
                    Log.i("Jth is", jthObj.toString());

                    Date ithDate = new Date(ithObj.getComTime());
                    Date jthDate = new Date(jthObj.getComTime());

                    int r = ithDate.compareTo(jthDate);
                    if(r < 0){

                        Log.i("Rm Jth", jthObj.toString());
                        removeIndexes.add(j);
                    } else if(r > 0){

                        Log.i("Rm Ith", ithObj.toString());
                        removeIndexes.add(i);
                    }
                }
//                else {
//                    Log.e("OUT if", "No Match Found!");
//                }

            }
        }

        Log.e("Remove Indxs", removeIndexes.size()+"    "+removeIndexes.toString());

        for(int k = 0 ; k<removeIndexes.size(); k++){

            int index = removeIndexes.get(k);

            Log.e("Removing", fusionFeed.get(index).toString());

            fusionFeed.remove(k);
        }

        Log.e("Fusion fees size", fusionFeed.size()+"");

        fusionAdapter.loadNews(fusionFeed);
    }

    public static Fragment newInstance(ArrayList<FusionObject> fusionFeedList) {

        FusionFragment fragment = new FusionFragment();

        Bundle args = new Bundle();
        args.putParcelableArrayList("fuFeed", fusionFeedList);
        fragment.setArguments(args);

        return fragment;
    }
}
