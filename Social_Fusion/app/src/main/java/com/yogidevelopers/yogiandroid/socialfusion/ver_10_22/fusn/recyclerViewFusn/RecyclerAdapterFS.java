package com.yogidevelopers.yogiandroid.socialfusion.ver_10_22.fusn.recyclerViewFusn;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.yogidevelopers.yogiandroid.socialfusion.R;

import java.util.Date;
import java.util.List;

import static com.yogidevelopers.yogiandroid.socialfusion.R.mipmap.ic_launcher;

/**
 * Created by yogi on 10/10/16.
 */
public class RecyclerAdapterFS extends RecyclerView.Adapter<MyFusionVH>{

    private List<FusionObject> news;
    private Context context;
    private LayoutInflater layoutInflater;

    public RecyclerAdapterFS(Context context, List<FusionObject> news) {
        this.context = context;
        this.news = news;

        this.layoutInflater = LayoutInflater.from(context);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyFusionVH onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = layoutInflater.inflate(R.layout.fusion_entry, parent, false);
        return new MyFusionVH(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyFusionVH holder, int position) {

        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        FusionObject newsObj = news.get(position);

        StringBuilder newsText = new StringBuilder();
        String newsPoster;

        if(newsObj.getObjType().equals("TW")) {
            newsPoster = newsObj.getTw_user_name();

            newsText.append(newsObj.getTw_post());
        }
        else {
            newsPoster = newsObj.getFb_creator();

            newsText.append(newsObj.getFb_story());
            newsText.append("\n");
            newsText.append(newsObj.getFb_message());
        }

        holder.newsPoster.setText(newsPoster+" on "+String.valueOf(new Date(newsObj.getComTime())));

        holder.newsText.setText(newsText);

        String imageUrl;
        if(newsObj.getObjType().equals("TW"))
            imageUrl = newsObj.getTw_media_url();
        else
            imageUrl = newsObj.getFb_picture();

        if (imageUrl != null && !imageUrl.isEmpty()) {

            holder.newsImage.setVisibility(View.VISIBLE);
            Picasso.with(context).load(imageUrl)
                    .error(ic_launcher)
                    .placeholder(ic_launcher)
                    .into(holder.newsImage);
        } else {
            holder.newsImage.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return (news != null ? news.size() : 0);
    }

    public void loadNews(List<FusionObject> newsList) {

        news = newsList;
        notifyDataSetChanged();
    }
}
