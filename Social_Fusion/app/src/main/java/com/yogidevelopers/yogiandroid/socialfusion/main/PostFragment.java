package com.yogidevelopers.yogiandroid.socialfusion.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;
import com.yogidevelopers.yogiandroid.socialfusion.R;

import retrofit2.Call;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment {


    private Button postFacebook;
    private Button postTweet;
    private EditText composeText;

    public PostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_post, container, false);

        postFacebook = (Button) v.findViewById(R.id.postToFacebook);
        postTweet = (Button) v.findViewById(R.id.postTweet);
        composeText = (EditText) v.findViewById(R.id.composePost);

        //Post to Facebook

        final AccessToken loggedIn = AccessToken.getCurrentAccessToken();

        postFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (composeText.getText().length() == 0) {

                    Toast.makeText(getContext(), "Ops! Seems like you forgot to compose...", Toast.LENGTH_LONG).show();
                } else {

                    postToFacebook(loggedIn, composeText.getText().toString());
                }
            }
        });


        //Post to Twitter

        final Session activeSession = Twitter.getSessionManager().getActiveSession();

        postTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (composeText.getText().length() == 0) {

                    Toast.makeText(getContext(), "Ops! Seems like you forgot to compose...", Toast.LENGTH_LONG).show();
                } else {

                    postToTwitter(activeSession, composeText.getText().toString());
                }
            }
        });

//        @FormUrlEncoded
//        @POST("/1.1/statuses/update.json")
//        void update(@Field("status") String var1,
//                    @Field("in_reply_to_status_id") Long var2,
//                    @Field("possibly_sensitive") Boolean var3,
//                    @Field("lat") Double var4,
//                    @Field("long") Double var5,
//                    @Field("place_id") String var6,
//                    @Field("display_cooridnates") Boolean var7,
//                    @Field("trim_user") Boolean var8,
//                    @Field("media_ids") String var9, Callback<Tweet> var10);

        return v;
    }

    private void postToTwitter(Session activeSession, String s) {

        if (activeSession != null) {

            TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
            StatusesService statusesService = twitterApiClient.getStatusesService();

            Call<Tweet> call = statusesService.update(s, null, null, null, null, null, null, null, null);
            call.enqueue(new Callback<Tweet>() {
                @Override
                public void success(Result<Tweet> result) {
                    Toast.makeText(getContext(), "Tweet sent!", Toast.LENGTH_LONG).show();
                }

                @Override
                public void failure(TwitterException exception) {

                    Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "Login Required!", Toast.LENGTH_LONG).show();
        }
    }

    private void postToFacebook(AccessToken loggedIn, String s) {

        if (loggedIn != null) {

            Bundle params = new Bundle();
            params.putString("message", s);

            new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/me/feed",
                    params,
                    HttpMethod.POST,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {

                            Toast.makeText(getContext(), "Post uploaded to !", Toast.LENGTH_LONG).show();
                        }
                    }
            ).executeAsync();

        } else {
            Toast.makeText(getContext(), "Login Required!", Toast.LENGTH_LONG).show();
        }
    }

}
