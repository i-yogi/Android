package com.yogidevelopers.yogiandroid.socialfusion.ver_10_22.twtr.recyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yogidevelopers.yogiandroid.socialfusion.R;

/**
 * Created by yogi on 10/9/16.
 */
public class MyTwViewHolder extends RecyclerView.ViewHolder {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public TextView twCreator;
    public TextView tweet;
    public ImageView tweetImage;

    public MyTwViewHolder(View itemView) {
        super(itemView);

        this.twCreator = (TextView) itemView.findViewById(R.id.twCreator);
        this.tweet = (TextView) itemView.findViewById(R.id.tweetText);
        this.tweetImage = (ImageView) itemView.findViewById(R.id.tweetImage);
    }
}
