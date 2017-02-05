package com.yogidevelopers.yogiandroid.socialfusion.ver_10_22.fcbk;

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

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.yogidevelopers.yogiandroid.socialfusion.ver_10_22.fcbk.recyclerView.FBObject;
import com.yogidevelopers.yogiandroid.socialfusion.ver_10_22.fcbk.recyclerView.RecyclerAdapterFB;
import com.yogidevelopers.yogiandroid.socialfusion.R;

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
public class FBLoginFragment extends Fragment {

    //initialize this single-tone object ...factory pattern... inside onCreateView()
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;

    private LoginButton loginButton;
    private RelativeLayout feedView;

    private Button getFeedButton;
    private EditText getLimitEText;

    private RecyclerView recyclerView;
    private RecyclerAdapterFB recyclerAdapterFB;
    private ArrayList<FBObject> feed = new ArrayList<>();

    public FBLoginFragment() {
        // Required empty public constructor
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        outState.putParcelableArrayList("FbFeedIn", feed);
//    }


//    @Override
//    public void onResume() {
//        super.onResume();
//
//        recyclerAdapterFB.loadNewFbPosts(feed);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fblogin, container, false);

        loginButton = (LoginButton) v.findViewById(R.id.login_button);

        feedView = (RelativeLayout) v.findViewById(R.id.feedinLogIn);

        callbackManager = CallbackManager.Factory.create();

        getFeedButton = (Button) v.findViewById(R.id.inputLimitIN);
        getLimitEText = (EditText) v.findViewById(R.id.inputTextIN);

        recyclerView = (RecyclerView) v.findViewById(R.id.FbRecyclerFeedIN);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerAdapterFB = new RecyclerAdapterFB(new ArrayList<FBObject>(), getActivity());
        recyclerView.setAdapter(recyclerAdapterFB);


//        if (savedInstanceState == null || !savedInstanceState.containsKey("FbFeedIn")) {
//            Log.e("SavedInstanceState", " Is Empty!");
//        } else {
//            Log.e("SavedInstanceState", " Success!");
//            feed = savedInstanceState.getParcelableArrayList("FbFeedIn");
//            recyclerAdapterFB.loadNewFbPosts(feed);
//        }


        recyclerAdapterFB.loadNewFbPosts(feed);

        AccessToken logedIn = AccessToken.getCurrentAccessToken();

        if (logedIn != null) {

            loadFeedView();

        } else {

            login();
        }

        return v;
    }

    private void login() {

        loginButton.setReadPermissions("public_profile", "user_posts");
        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getProfile();

//                downloadFbData.fragToMainFbData("From Login Frag to Main!");

                loadFeedView();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getContext(), "Login Cancelled!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getContext(), "Login Error", Toast.LENGTH_LONG).show();
            }
        });

        loginButton.clearPermissions();
        loginButton.setPublishPermissions("publish_actions");
    }

    private void loadFeedView() {

        loginButton.setVisibility(View.GONE);
        feedView.setVisibility(View.VISIBLE);


        final Bundle params = new Bundle();
        params.putString("fields", "id,from,created_time,message,story,caption,link,name,picture,permalink_url,message_tags");

        getFeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getLimitEText.getText().length() == 0) {
                    if (feed.size() != 0)
                        recyclerAdapterFB.loadNewFbPosts(feed);
                    else
                        getFbData(params);

                } else {

                    feed.clear();

                    String limit = getLimitEText.getText().toString();
                    params.putString("limit", limit);

                    getFbData(params);
                }
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });


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
                        parseResponse(fbResponse);
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

            recyclerAdapterFB.loadNewFbPosts(feed);
        }
    }

    private Date getDate(String timeString) {

        SimpleDateFormat fb_dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZZZZZ");

        long when = 0;
        try {
            when = fb_dateFormat.parse(timeString).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date localDate = new Date(when + TimeZone.getDefault().getRawOffset() +
                (TimeZone.getDefault().inDaylightTime(new Date()) ? TimeZone.getDefault().getDSTSavings() : 0));

        return localDate;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void getProfile() {

        if (Profile.getCurrentProfile() == null) {
            profileTracker = new ProfileTracker() {
                @Override
                protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                    Toast.makeText(getContext(), "Welcome, " + currentProfile.getName(), Toast.LENGTH_LONG).show();
                    profileTracker.stopTracking();
                }
            };
        } else {
            Profile profile = Profile.getCurrentProfile();
            Toast.makeText(getContext(), "Welcome, " + profile.getName(), Toast.LENGTH_LONG).show();
        }
    }

}
