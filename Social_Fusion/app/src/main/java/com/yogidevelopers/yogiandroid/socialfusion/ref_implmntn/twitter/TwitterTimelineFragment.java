package com.yogidevelopers.yogiandroid.socialfusion.ref_implmntn.twitter;


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
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.yogidevelopers.yogiandroid.socialfusion.R;
import com.yogidevelopers.yogiandroid.socialfusion.ver_10_22.twtr.MyTwitterApiClient;
import com.yogidevelopers.yogiandroid.socialfusion.ver_10_22.twtr.recyclerView.RecyclerAdapterTW;
import com.yogidevelopers.yogiandroid.socialfusion.ver_10_22.twtr.recyclerView.TweetObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


/**
 * A simple {@link Fragment} subclass.
 */
public class TwitterTimelineFragment extends Fragment {

//    public interface TwToMain{
//        void twFeed(ArrayList<TweetObject> tweetsList);
//    }
//
//    TwToMain sendTweetsToFuse;
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//        try {
//            sendTweetsToFuse = (TwToMain) context;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString());
//        }
//    }

//    private List<TweetObject> tweets = new ArrayList<>();

    private int counter = 0;

    private ArrayList<TweetObject> tweets = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerAdapterTW recyclerAdapterTW;

    public TwitterTimelineFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_twittertimeline, container, false);

//        tweets = getArguments().getParcelableArrayList("feed");

        recyclerView = (RecyclerView) v.findViewById(R.id.TWRecyclerFeed);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerAdapterTW = new RecyclerAdapterTW(new ArrayList<TweetObject>(), getActivity());
        recyclerView.setAdapter(recyclerAdapterTW);

        recyclerAdapterTW.loadNewTweets(tweets);


        final EditText inTextLimit = (EditText) v.findViewById(R.id.inputTw);
        final Button inLimitButton = (Button) v.findViewById(R.id.inputTwLimit);

        inLimitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (counter < 7) {

                    counter += 1;

                    if (inTextLimit.getText().length() == 0) {

                        tweets.clear();

                        getTwData("20");

                        Log.e("get tw pressed", "No limit in!");

                    } else {

                        tweets.clear();

                        getTwData(inTextLimit.getText().toString());
                    }

                    recyclerView.getAdapter().notifyDataSetChanged();
                } else {

                    inLimitButton.setVisibility(View.GONE);

                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    Toast.makeText(getContext(), "Requests to server available", Toast.LENGTH_LONG).show();
                                    counter = 0;

                                }
                            },
                            900000);
                    inLimitButton.setVisibility(View.VISIBLE);
                    recyclerAdapterTW.loadNewTweets(tweets);
                    Toast.makeText(getContext(), "Please limit no of requests to server!", Toast.LENGTH_LONG).show();
            }
        }
    }

    );

    return v;
}

    private void getTwData(String s) {

        TwitterSession session = Twitter.getSessionManager().getActiveSession();
        new MyTwitterApiClient(session).getHomeTimeline().show(s).enqueue(new Callback<List>() {
            @Override
            public void success(Result<List> result) {
//                Log.e("Twitter Success: ", "Yey RESPONCE!" + result.response);

                List array = new ArrayList(result.data);
                parseJson(array);
            }

            @Override
            public void failure(TwitterException exception) {

            }
        });

    }

    private void parseJson(List array) {

        if (!array.isEmpty()) {

            String id, timeString, post, user_name = "", user_image = "", user_id = "";
            Date time;

            try {
                JSONArray tweets = new JSONArray(array);

                for (int i = 0; i < tweets.length(); i++) {
                    JSONObject curTweet = tweets.getJSONObject(i);

                    String media_url = "";

                    if (curTweet.has("created_at")) {
                        timeString = curTweet.getString("created_at");
                        time = getDate(timeString);
                    } else
                        time = null;

                    if (curTweet.has("id_str"))
                        id = curTweet.getString("id_str");
                    else
                        id = "";

                    if (curTweet.has("text"))
                        post = curTweet.getString("text");
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

                    TweetObject tweet = new TweetObject(id, time, post,
                            user_image, user_id, user_name, media_url);
                    this.tweets.add(tweet);
//                    Log.e("Tweet added: ", tweetFeed.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

//            sendTweetsToFuse.twFeed(tweets);
            recyclerAdapterTW.loadNewTweets(tweets);
        }
    }

    private Date getDate(String timeString) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

//        SimpleDateFormat fb_dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZZZZZ");
//        String TWITTER_date="EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat twitter_date = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");

//        SimpleDateFormat my_dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        String localDateString = null;

        long when = 0;
        try {
            when = twitter_date.parse(timeString).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        localDateString = dateFormat.format(new Date(when + TimeZone.getDefault().getRawOffset() +
//                (TimeZone.getDefault().inDaylightTime(new Date()) ? TimeZone.getDefault().getDSTSavings() : 0)));
//
//        return localDateString;
        Date localDate = new Date(when + TimeZone.getDefault().getRawOffset() +
                (TimeZone.getDefault().inDaylightTime(new Date()) ? TimeZone.getDefault().getDSTSavings() : 0));

        return localDate;

    }

//    public static Fragment newInstance(ArrayList<TweetObject> twitterFeed) {
//
//        TwitterTimelineFragment fragment = new TwitterTimelineFragment();
//
//        Bundle args = new Bundle();
//        args.putParcelableArrayList("feed", twitterFeed);
//        fragment.setArguments(args);
//
//        return fragment;
//    }

}
