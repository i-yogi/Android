package com.yogidevelopers.yogiandroid.socialfusion.ver_10_22.fcbk.recyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yogidevelopers.yogiandroid.socialfusion.R;

/**
 * Created by yogi on 9/12/16.
 */
public class MyViewHolder extends RecyclerView.ViewHolder {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public TextView poster;
    public TextView post;
    public ImageView image;

    public MyViewHolder(View itemView) {
        super(itemView);

        this.post = (TextView) itemView.findViewById(R.id.fbpost);
        this.poster = (TextView) itemView.findViewById(R.id.fbposter);
        this.image = (ImageView) itemView.findViewById(R.id.postImage);
    }
}
