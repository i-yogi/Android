package com.yogidevelopers.yogiandroid.socialfusion.ver_10_22.fusn.recyclerViewFusn;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yogidevelopers.yogiandroid.socialfusion.R;

/**
 * Created by yogi on 10/10/16.
 */
public class MyFusionVH extends RecyclerView.ViewHolder{

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public TextView newsPoster;
    public TextView newsText;
    public ImageView newsImage;

    public MyFusionVH(View itemView) {
        super(itemView);

        this.newsPoster = (TextView) itemView.findViewById(R.id.newsPoster);
        this.newsText = (TextView) itemView.findViewById(R.id.newsText);
        this.newsImage = (ImageView) itemView.findViewById(R.id.newsImage);
    }
}
