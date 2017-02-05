package com.yogidevelopers.yogiandroid.socialfusion.ref_implmntn.fusion;

import com.yogidevelopers.yogiandroid.socialfusion.ver_10_22.fusn.recyclerViewFusn.FusionObject;

import java.util.ArrayList;

/**
 * Created by yogi on 10/17/16.
 */
public class DownloadFusionFb {

    private int requestCount = 0;
    private ArrayList<FusionObject> feed;

    public DownloadFusionFb() {

        feed = new ArrayList<>();
    }

    public ArrayList<FusionObject> getFeed() {

        if (feed.size() == 0) {
//            getFbFeed();
            return feed;
        } else {
            return feed;
        }
    }


}
