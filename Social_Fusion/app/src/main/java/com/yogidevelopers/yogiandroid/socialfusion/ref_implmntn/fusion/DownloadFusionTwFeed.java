package com.yogidevelopers.yogiandroid.socialfusion.ref_implmntn.fusion;

import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.yogidevelopers.yogiandroid.socialfusion.ver_10_22.twtr.MyTwitterApiClient;
import com.yogidevelopers.yogiandroid.socialfusion.ver_10_22.fusn.recyclerViewFusn.FusionObject;

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
 * Created by yogi on 10/16/16.
 */

public class DownloadFusionTwFeed {

    private int requestCount = 0;


    private ArrayList<FusionObject> fusionTw;

    public DownloadFusionTwFeed() {

        fusionTw = new ArrayList<>();
    }

    public ArrayList<FusionObject> getFeed() {

        if (fusionTw.size() == 0) {
            getFusionData();
            return fusionTw;
        } else {
            return fusionTw;
        }
    }

    private void getFusionData() {

        requestCount++;
        Log.e("Fusion tw Feed Request", String.valueOf(requestCount));

        getTwFusion();
        getFbFusion();
    }

    private void getFbFusion() {

        Bundle params = new Bundle();
//        params.putString("fields", "id,created_time,story,message,link,picture,full_picture");
        params.putString("fields", "id,from,created_time,message,story,caption,link,name,picture,permalink_url,message_tags");
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

    private void parseResponse(JSONObject fbResponse) {

        if (fbResponse != null) {
            String id, creator = "", timeString, story, message, hashTags, picture, url;
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

//                    FBObject post = new FBObject(id, creator, time, story, message, picture, url);
                    FusionObject fbFusionObj = new FusionObject("FB", time, hashTags, id, creator, story, message, picture, url,
                            null, null, null, null, null);

//                    Log.e("Fb fusion time", String.valueOf(new Date(time)));

                    this.fusionTw.add(fbFusionObj);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            recyclerAdapterFB.loadNewFbPosts(feed);
//            Log.e("Fb post: ", fusionTw.toString());
        }
    }

    private String addTags(String message) {


        StringBuilder tags = new StringBuilder("");

        if (message.length() > 0) {

            String words[] = message.split(" ");

            for (String w : words) {
                if (w.startsWith("#"))
                    tags.append(w);
//                Log.e("w", w);
            }
        }

//        Log.e("Tags", tags.toString());
        return tags.toString();
    }

    private long getFbDate(String timeString) {
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

        Date localDate = new Date(when + TimeZone.getDefault().getRawOffset() +
                (TimeZone.getDefault().inDaylightTime(new Date()) ? TimeZone.getDefault().getDSTSavings() : 0));

        return localDate.getTime();
    }

    private void getTwFusion() {

        TwitterSession session = Twitter.getSessionManager().getActiveSession();
        new MyTwitterApiClient(session).getHomeTimeline().show("15").enqueue(new Callback<List>() {
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

            String id, timeString, hTags = "", post, user_name = "", user_image = "", user_id = "";
            long time;

            try {
                JSONArray tweets = new JSONArray(array);

                for (int i = 0; i < tweets.length(); i++) {
                    JSONObject curTweet = tweets.getJSONObject(i);

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

                        if(entities.has("hashtags")){
                            StringBuilder tags = new StringBuilder("");

                            JSONArray hashtags = entities.getJSONArray("hashtags");
                            for (int j = 0; j < hashtags.length(); j++) {

                                JSONObject curTag = hashtags.getJSONObject(j);

                                if(curTag.has("text")){
                                    tags.append("#"+curTag.getString("text"));
                                }
                            }
//                            Log.e("tw tags", hTags.toString());
                            hTags = tags.toString();
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

                    FusionObject fusionObj = new FusionObject("TW", time, hTags, user_id, null, null, null, null, null,
                            post, media_url, user_name, user_id, user_image);
//                    Log.e("Tw fusion time", String.valueOf(new Date(time)));

                    this.fusionTw.add(fusionObj);
//                    Log.e("Tweet added: ", tweetFeed.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private long getTwDate(String timeString) {

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

        return localDate.getTime();
    }

}
