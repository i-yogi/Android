package com.yogidevelopers.yogiandroid.socialfusion.ver_10_22.twtr;

import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterSession;

public class MyTwitterApiClient extends TwitterApiClient {
    public MyTwitterApiClient(TwitterSession session) {
        super(session);
    }

    public HomeTimeline getHomeTimeline() {
        return getService(HomeTimeline.class);
    }
}
