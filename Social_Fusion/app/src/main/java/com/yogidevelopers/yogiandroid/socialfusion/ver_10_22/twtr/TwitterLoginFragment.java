package com.yogidevelopers.yogiandroid.socialfusion.ver_10_22.twtr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.yogidevelopers.yogiandroid.socialfusion.R;
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
public class TwitterLoginFragment extends Fragment {

    private TwitterLoginButton loginButton;

    private RelativeLayout tweeterFeedView;

    private EditText tweetLimitEText;
    private Button setTweetLimit;

    private ArrayList<TweetObject> tweets = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerAdapterTW recyclerAdapterTW;

    private int counter = 0;

    public TwitterLoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_twtlogin, container, false);

        loginButton = (TwitterLoginButton) v.findViewById(R.id.twitter_login_button);
        tweeterFeedView = (RelativeLayout) v.findViewById(R.id.tweetIn);

        tweetLimitEText = (EditText) v.findViewById(R.id.limitTextIn);
        setTweetLimit = (Button) v.findViewById(R.id.limitSetButton);

        recyclerView = (RecyclerView) v.findViewById(R.id.TwRecyclerViewIn);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerAdapterTW = new RecyclerAdapterTW(new ArrayList<TweetObject>(), getActivity());
        recyclerView.setAdapter(recyclerAdapterTW);

//        tweets = getArguments().getParcelableArrayList("twFeed");

        recyclerAdapterTW.loadNewTweets(tweets);

        Session activeSession = Twitter.getSessionManager().getActiveSession();

        if (activeSession != null) {

            getTweets();

        } else {

            twitterLogin();
        }


        return v;
    }

    private void twitterLogin() {

        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();

                getTweets();
            }

            @Override
            public void failure(TwitterException exception) {
                String msgTe = exception.toString();
                Toast.makeText(getContext(), msgTe, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        loginButton.onActivityResult(requestCode, resultCode, data);

    }

    private void getTweets() {

        loginButton.setVisibility(View.GONE);
        tweeterFeedView.setVisibility(View.VISIBLE);

        if(counter < 7)
            setTweetLimit.setVisibility(View.VISIBLE);

        setTweetLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (counter < 7) {

                    counter += 1;

                    if (tweetLimitEText.getText().length() == 0) {


                        if(tweets.size() != 0)
                            recyclerAdapterTW.loadNewTweets(tweets);
                        else
                            getTwData("20");

                    } else {

                        tweets.clear();
                        getTwData(tweetLimitEText.getText().toString());
                    }

                    recyclerView.getAdapter().notifyDataSetChanged();
                } else {

                    setTweetLimit.setVisibility(View.GONE);

                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    Toast.makeText(getContext(), "Requests to server available", Toast.LENGTH_LONG).show();
                                    counter = 0;
                                    setTweetLimit.setVisibility(View.VISIBLE);
                                }
                            },
                            900000);

                    recyclerAdapterTW.loadNewTweets(tweets);
                    Toast.makeText(getContext(), "Please limit no of requests to server!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void getTwData(String s) {

        TwitterSession session = Twitter.getSessionManager()
                .getActiveSession();
        new MyTwitterApiClient(session).getHomeTimeline().show(s)
                .enqueue(new Callback<List>() {
            @Override
            public void success(Result<List> result) {
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

            recyclerAdapterTW.loadNewTweets(tweets);
        }
    }

    private Date getDate(String timeString) {

        SimpleDateFormat twitter_date = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");

        long when = 0;
        try {
            when = twitter_date.parse(timeString).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date localDate = new Date(when + TimeZone.getDefault().getRawOffset() +
                (TimeZone.getDefault().inDaylightTime(new Date()) ? TimeZone.getDefault().getDSTSavings() : 0));

        return localDate;

    }
}
