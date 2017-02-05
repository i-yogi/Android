package com.yogidevelopers.yogiandroid.socialfusion.ref_implmntn.facebook;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.yogidevelopers.yogiandroid.socialfusion.R;
import com.yogidevelopers.yogiandroid.socialfusion.ver_10_22.fcbk.recyclerView.FBObject;
import com.yogidevelopers.yogiandroid.socialfusion.ver_10_22.fcbk.recyclerView.RecyclerAdapterFB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;


/**
 * A simple {@link Fragment} subclass.
 */
public class FBFeedFragment extends Fragment {

//    public interface FbToMain{
//        void fbFeed(ArrayList<FBObject> fbFeed);
//    }
//
//    FbToMain sendFeedToFuse;
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//        try {
//            sendFeedToFuse = (FbToMain) context;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString());
//        }
//    }

    private ArrayList<FBObject> feed = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerAdapterFB recyclerAdapterFB;

    public FBFeedFragment() {
        // Required empty public constructor
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        recyclerAdapterFB.loadNewFbPosts(feed);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fbfeed, container, false);

//        feed = getArguments().getParcelableArrayList("fbFeed");

        recyclerView = (RecyclerView) v.findViewById(R.id.FbRecyclerFeed);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerAdapterFB = new RecyclerAdapterFB(new ArrayList<FBObject>(), getActivity());
        recyclerView.setAdapter(recyclerAdapterFB);

        final EditText postLimit = (EditText) v.findViewById(R.id.inputText);

        Button limitSet = (Button) v.findViewById(R.id.inputLimit);

        final Bundle params = new Bundle();
        params.putString("fields", "id,from,created_time,message,story,caption,link,name,picture,permalink_url,message_tags");
//        params.putString("limit", "2");

        limitSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(postLimit.getText().length() == 0){

                    feed.clear();

//                    params.putString("fields", "id,from,created_time,message,story,caption,link,name,picture,permalink_url,message_tags");
//                    Log.e("NullLimit Param is", params.toString());

                    getFbData(params);
                } else{

                    feed.clear();

                    String limit = postLimit.getText().toString();

//                    String paramString = "id,from,created_time,message,story,caption,link,name,picture,permalink_url,message_tags");
                    params.putString("limit", limit);
//                    Log.e("Limit Param is", params.toString());

                    getFbData(params);
                }

                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });

//        getFbData(params);

        return v;
    }

    private void getFbData(Bundle params) {

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/feed",
                params,
                HttpMethod.GET,
                new GraphRequest.Callback() {

                    public void onCompleted(GraphResponse response) {

                        JSONObject fbResponse = response.getJSONObject();
//                        jsonText.setText(fbResponse.toString());
                        parseResponse(fbResponse);
//                        Log.e("Fb response: ", String.valueOf(response));
                    }
                }
        ).executeAsync();
    }

    private void parseResponse(JSONObject jsonObject) {

        if (jsonObject != null) {
            String id, creator = "", timeString, story, message, picture, url;
            Date time;
            JSONObject from;

            try {
                JSONArray data = jsonObject.getJSONArray("data");

                for (int i = 0; i < data.length(); i++) {

                    JSONObject current = data.getJSONObject(i);

                    if (current.has("id"))
                        id = current.getString("id");
                    else
                        id = "";

                    if (current.has("from")) {
                        from = current.getJSONObject("from");

                        creator = from.getString("name");
                    } else
                        creator = null;

                    if (current.has("created_time")) {
                        timeString = current.getString("created_time");
                        time = getDate(timeString);
                    } else {
                        time = null;
                    }

                    if (current.has("story"))
                        story = current.getString("story");
                    else
                        story = "";

                    if (current.has("message"))
                        message = current.getString("message");
                    else
                        message = "";

                    if (current.has("picture"))
                        picture = current.getString("picture");
                    else
                        picture = "";

                    if (current.has("permalink_url"))
                        url = current.getString("permalink_url");
                    else
                        url = "";

                    FBObject post = new FBObject(id, creator, time, story, message, picture, url);

                    this.feed.add(post);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

//            sendFeedToFuse.fbFeed(feed);
            recyclerAdapterFB.loadNewFbPosts(feed);
//            Log.e("Fb post: ", feed.toString());
        }

    }

    private Date getDate(String timeString) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        SimpleDateFormat fb_dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZZZZZ");

//        SimpleDateFormat my_dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        String localDateString = null;

        long when = 0;
        try {
            when = fb_dateFormat.parse(timeString).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        localDateString = dateFormat.format(new Date(when + TimeZone.getDefault().getRawOffset() +
//                (TimeZone.getDefault().inDaylightTime(new Date()) ? TimeZone.getDefault().getDSTSavings() : 0)));
//        Log.e("String date", localDateString.toString());

        Date localDate = new Date(when + TimeZone.getDefault().getRawOffset() +
                (TimeZone.getDefault().inDaylightTime(new Date()) ? TimeZone.getDefault().getDSTSavings() : 0));
//        Log.e("Date", localDate.toString());

//        Long dateLong = Long.getLong(localDateString);
//        Log.e("Date long", localDateString+" "+dateLong);

        return localDate;
    }


//    public static Fragment newInstance(ArrayList<FBObject> facebookFeed) {
//
//        FBFeedFragment fragment = new FBFeedFragment();
//
//        Bundle args = new Bundle();
//        args.putParcelableArrayList("fbFeed", facebookFeed);
//        fragment.setArguments(args);
//
//        return fragment;
//    }
}
