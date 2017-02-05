package com.yogidevelopers.yogiandroid.socialfusion.ver_10_22.fusn;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.yogidevelopers.yogiandroid.socialfusion.R;
import com.yogidevelopers.yogiandroid.socialfusion.ver_10_22.twtr.MyTwitterApiClient;
import com.yogidevelopers.yogiandroid.socialfusion.ver_10_22.fusn.recyclerViewFusn.FusionObject;
import com.yogidevelopers.yogiandroid.socialfusion.ver_10_22.fusn.recyclerViewFusn.RecyclerAdapterFS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 */
public class FusionOptFrag extends Fragment {

    private TextView emptyView;
    private RelativeLayout feedView;

    private Button getFeed;
    private EditText setLimitText;

    private int counter = 0;

    private RecyclerView recyclerView;
    private RecyclerAdapterFS fusionAdapter;
    private ArrayList<FusionObject> fusioFeed = new ArrayList<>();

    public FusionOptFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_optfusion, container, false);

        emptyView = (TextView) v.findViewById(R.id.emptyFusion);
        feedView = (RelativeLayout) v.findViewById(R.id.fusionRelative);

        setLimitText = (EditText) v.findViewById(R.id.limitInFusion);
        getFeed = (Button) v.findViewById(R.id.limitFSetButton);

        recyclerView = (RecyclerView) v.findViewById(R.id.FusnRecyclerViewIn);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        fusionAdapter = new RecyclerAdapterFS(getActivity(), new ArrayList<FusionObject>());
        recyclerView.setAdapter(fusionAdapter);

        fusionAdapter.loadNews(fusioFeed);

        AccessToken fbLoggedIn = AccessToken.getCurrentAccessToken();                  //Facebook
        Session twitterInSession = Twitter.getSessionManager().getActiveSession();     //Twitter

        if (fbLoggedIn != null && twitterInSession != null) {

            emptyView.setVisibility(View.GONE);
            feedView.setVisibility(View.VISIBLE);

            loadData();
        } else {

            feedView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }

        return v;
    }

    private void loadData() {

        if(counter < 7)
            getFeed.setVisibility(View.VISIBLE);

        getFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (counter < 7) {

                    counter += 1;

                    if (setLimitText.getText().length() == 0) {

                        if (fusioFeed.size() != 0)
                            fusionAdapter.loadNews(fusioFeed);
                        else
                            loadDefaultData();

                    } else {

                        fusioFeed.clear();
                        loadSpecfData(setLimitText.getText().toString());
                    }

                    recyclerView.getAdapter().notifyDataSetChanged();
                } else {

                    getFeed.setVisibility(View.GONE);

                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    Toast.makeText(getContext(), "Requests to server available", Toast.LENGTH_LONG).show();
                                    counter = 0;
                                    getFeed.setVisibility(View.VISIBLE);
                                }
                            },
                            900000);

                    fusionAdapter.loadNews(fusioFeed);
                    Toast.makeText(getContext(), "Please limit no of requests to server!", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    private void loadDefaultData() {

        getFbData("20");
        getTwData("20");
        processFeed();
    }

    private void loadSpecfData(String s) {

        getFbData(s);
        getTwData(s);
        processFeed();
    }

    private void getTwData(String s) {

        TwitterSession session = Twitter.getSessionManager().getActiveSession();
        new MyTwitterApiClient(session).getHomeTimeline().show(s).enqueue(new Callback<List>() {
            @Override
            public void success(Result<List> result) {
//                Log.e("Twitter Success: ", "Yey RESPONCE!" + result.response);

                List array = new ArrayList(result.data);
                parseTwJson(array);
            }

            @Override
            public void failure(TwitterException exception) {

            }
        });
    }

    private void parseTwJson(List array) {

        if (!array.isEmpty()) {

            String id, timeString, hTags = "", post, user_name = "", user_image = "", user_id = "";
            long time;

            try {
                JSONArray tweets = new JSONArray(array);

                for (int i = 0; i < tweets.length(); i++) {
                    JSONObject curTweet = tweets.getJSONObject(i);
                    StringBuilder tags = new StringBuilder("");

                    String media_url = "";

                    if (curTweet.has("created_at")) {
                        timeString = curTweet.getString("created_at");
                        time = getTwDate(timeString);
                    } else
                        time = 0;

                    if (curTweet.has("id_str"))
                        id = curTweet.getString("id_str");
                    else
                        id = "";

                    if (curTweet.has("text")) {
                        post = curTweet.getString("text");
                        tags.append(addTags(post));
                    }
                    else
                        post = "";

                    if (curTweet.has("entities")) {

                        JSONObject entities = curTweet.getJSONObject("entities");

                        if (entities.has("media")) {

                            JSONArray media = entities.getJSONArray("media");

                            JSONObject mediaObj = media.getJSONObject(0);

                            if (mediaObj.has("media_url"))
                                media_url = mediaObj.getString("media_url");
                        }
                    }

                    if (curTweet.has("user")) {
                        JSONObject user = curTweet.getJSONObject("user");

                        if (user.has("id_str"))
                            user_id = user.getString("id_str");

                        if (user.has("name"))
                            user_name = user.getString("name");

                        if (user.has("profile_image_url"))
                            user_image = user.getString("profile_image_url");
                    }

                    hTags = tags.toString();
                    FusionObject fusionObj = new FusionObject("TW", time, hTags, user_id, null, null, null, null, null,
                            post, media_url, user_name, user_id, user_image);
//                    Log.e("Tw fusion time", String.valueOf(new Date(time)));


                    this.fusioFeed.add(fusionObj);
                    Log.e("Tw ht: ", fusionObj.getHashTags());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        processFeed();
    }

    private long getTwDate(String timeString) {
        SimpleDateFormat twitter_date = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");
        long when = 0;
        try {
            when = twitter_date.parse(timeString).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date localDate = new Date(when + TimeZone.getDefault().getRawOffset() +
                (TimeZone.getDefault().inDaylightTime(new Date()) ? TimeZone.getDefault().getDSTSavings() : 0));

        return localDate.getTime();
    }

    private void getFbData(String s) {

        Bundle params = new Bundle();
        params.putString("fields", "id,from,created_time,message,story,caption,link,name,picture,permalink_url,message_tags");
        params.putString("limit", s);
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/feed",
                params,
                HttpMethod.GET,
                new GraphRequest.Callback() {

                    public void onCompleted(GraphResponse response) {

                        JSONObject fbResponse = response.getJSONObject();
                        parseFbResponse(fbResponse);
                    }
                }
        ).executeAsync();

    }

    private void parseFbResponse(JSONObject fbResponse) {

        if (fbResponse != null) {
            String id, creator, timeString, story, message, hashTags, picture, url;
            JSONObject from;
            long time;

            try {
                JSONArray data = fbResponse.getJSONArray("data");

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
                        time = getFbDate(timeString);
                    } else {
                        time = 0;
                    }

                    if (current.has("story"))
                        story = current.getString("story");
                    else
                        story = "";

                    if (current.has("message")) {
                        message = current.getString("message");
                        hashTags = addTags(message);
                    } else {
                        message = "";
                        hashTags = "";
                    }

                    if (current.has("picture"))
                        picture = current.getString("picture");
                    else
                        picture = "";

                    if (current.has("permalink_url"))
                        url = current.getString("permalink_url");
                    else
                        url = "";

                    FusionObject fbFusionObj = new FusionObject("FB", time, hashTags, id, creator, story, message, picture, url,
                            null, null, null, null, null);

                    this.fusioFeed.add(fbFusionObj);
                    Log.e("Fb ht: ", fbFusionObj.getHashTags());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        processFeed();
    }

    private long getFbDate(String timeString) {

        SimpleDateFormat fb_dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZZZZZ");
        long when = 0;
        try {
            when = fb_dateFormat.parse(timeString).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date localDate = new Date(when + TimeZone.getDefault().getRawOffset() +
                (TimeZone.getDefault().inDaylightTime(new Date()) ? TimeZone.getDefault().getDSTSavings() : 0));

        return localDate.getTime();
    }

    private String addTags(String message) {

        StringBuilder tags = new StringBuilder("");

        if (message.length() > 0) {

            String words[] = message.split(" ");

            for (String w : words) {
                if (w.startsWith("#"))
                    tags.append(w);
            }
        }
        return tags.toString();
    }


    private void processFeed() {

        Collections.sort(fusioFeed, FusionObject.PostDate);
        Collections.reverse(fusioFeed);

        ArrayList<Integer> removeIndexes = new ArrayList<>();

        for (int i = 0; i < fusioFeed.size()-1; i++) {

            FusionObject ithObj = fusioFeed.get(i);

            for (int j = i + 1; j < fusioFeed.size(); j++) {

                FusionObject jthObj = fusioFeed.get(j);

                if (ithObj.getHashTags().compareTo(jthObj.getHashTags()) == 0
                        && !ithObj.getObjType().equals(jthObj.getObjType())
                        && !ithObj.getHashTags().equals("")
                        && !jthObj.getHashTags().equals("")
                        && !ithObj.getId().equals(jthObj.getId())) {

                    Date ithDate = new Date(ithObj.getComTime());
                    Date jthDate = new Date(jthObj.getComTime());

                    int r = ithDate.compareTo(jthDate);
                    Log.e("Times", "i th"+ithDate+" - j is"+jthDate+" result is: "+r);

                    if (r < 0) {

                        Log.i("Rm Ith", ithObj.toString());
                        removeIndexes.add(i);
                    } else {

                        Log.i("Rm Jth", jthObj.toString());
                        removeIndexes.add(j);
                    }
                }
            }
        }

        for (int k = 0; k < removeIndexes.size(); k++) {

            int index = removeIndexes.get(k);

            fusioFeed.remove(k);
            Log.e("Removing", fusioFeed.get(index).toString());

            recyclerView.getAdapter().notifyDataSetChanged();
        }

        fusionAdapter.loadNews(fusioFeed);
    }
}
