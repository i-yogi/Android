package com.yogidevelopers.yogiandroid.socialfusion.main;

import android.app.Application;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

/**
 * Created by yogi on 10/2/16.
 */
public class App extends Application {

    private static final String TWITTER_KEY = "9R0DaiMBjAJkHFYXC8JSHdJMB";
    private static final String TWITTER_SECRET = "WnD02hJV0ykkytYCZUG9WxSlaH6H2zWXG8wRVSrrkXIgVDxzew";

    private static App singleton;

    public static App getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        singleton = this;

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
    }
}
