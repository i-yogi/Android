package com.yogidevelopers.yogiandroid.socialfusion.ver_10_22.twtr.recyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.yogidevelopers.yogiandroid.socialfusion.R;

import java.util.List;

import static com.yogidevelopers.yogiandroid.socialfusion.R.mipmap.ic_launcher;

/**
 * Created by yogi on 10/9/16.
 */
public class RecyclerAdapterTW extends RecyclerView.Adapter<MyTwViewHolder> {

    private List<TweetObject> tweets;
    private Context context;
    private LayoutInflater layoutInflater;

    public RecyclerAdapterTW(List<TweetObject> tweets, Context context) {
        this.tweets = tweets;
        this.context = context;

        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MyTwViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = layoutInflater.inflate(R.layout.tw_single_tweet, parent, false);
        return new MyTwViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyTwViewHolder holder, int position) {

        TweetObject curTweet = tweets.get(position);

        holder.twCreator.setText(curTweet.getUser_name()+" on "+curTweet.getTime());
        holder.tweet.setText(curTweet.getPost()+"\n"+curTweet.getMedia_url());

        if (curTweet.getMedia_url() != null && !curTweet.getMedia_url().isEmpty()) {

            holder.tweetImage.setVisibility(View.VISIBLE);
            Picasso.with(context).load(curTweet.getMedia_url())
                    .error(ic_launcher)
                    .placeholder(ic_launcher)
                    .into(holder.tweetImage);
        } else {
            holder.tweetImage.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return (tweets != null ? tweets.size() : 0);
    }

    public void loadNewTweets(List<TweetObject> feed) {

        tweets = feed;
        notifyDataSetChanged();
    }
}
