package com.yogidevelopers.yogiandroid.socialfusion.ver_10_22.twtr;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HomeTimeline {

    @GET("/1.1/statuses/home_timeline.json")
    Call<List> show(@Query("count") String count);
}
