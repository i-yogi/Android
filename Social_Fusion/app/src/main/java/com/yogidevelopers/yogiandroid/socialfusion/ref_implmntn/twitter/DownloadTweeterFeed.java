package com.yogidevelopers.yogiandroid.socialfusion.ref_implmntn.twitter;

import android.util.Log;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.yogidevelopers.yogiandroid.socialfusion.ver_10_22.twtr.MyTwitterApiClient;
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
 * Created by yogi on 10/15/16.
 */
public class DownloadTweeterFeed {

    private int requestCount = 0;

    private ArrayList<TweetObject> tweetFeed;

    public DownloadTweeterFeed() {
        tweetFeed = new ArrayList<>();
    }

    public ArrayList<TweetObject> getFeed(String limit) {

        if (tweetFeed.size() == 0)
            getTwitterData(limit);

        return tweetFeed;
    }

    private void getTwitterData(String limit) {

        requestCount++;
        Log.e("Tweeter Feed Request", String.valueOf(requestCount));

        TwitterSession session = Twitter.getSessionManager().getActiveSession();

//        new MyTwitterApiClient(session).getHomeTimeline().show().enqueue(new Callback<List>() {
//            @Override
//            public void success(Result<List> result) {
////                Log.e("Twitter Success: ", "Yey RESPONCE!" + result.response);
//
//                List array = new ArrayList(result.data);
//                parseJson(array);
//            }
//
//            @Override
//            public void failure(TwitterException exception) {
//                Log.e("Twitter Failure: ", "No RESPONCE!" + exception.toString());
//            }
//        });

        new MyTwitterApiClient(session).getHomeTimeline().show(limit).enqueue(new Callback<List>() {
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

//                    if (curTweet.has("created_at"))
//                        time = curTweet.getString("created_at");
//                    else
//                        time = "";

                    if (curTweet.has("created_at")) {
                        timeString = curTweet.getString("created_at");
                        time = getDate(timeString);
                    } else
                        time = null;


//                    if (current.has("created_time")) {
//                        timeString = current.getString("created_time");
//                        time = getDate(timeString);
//                    } else {
//                        time = null;
//                    }


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
//                            Log.e("Media Obj: " + tweets.length() + " " + mediaObj.length(), media_url);
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
                    tweetFeed.add(tweet);
//                    Log.e("Tweet added: ", tweetFeed.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
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


}
