package com.yogidevelopers.yogiandroid.socialfusion.ver_10_22.fcbk.recyclerView;

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
 * Created by yogi on 9/12/16.
 */
public class RecyclerAdapterFB extends RecyclerView.Adapter<MyViewHolder> {

    private List<FBObject> dataset;
    private Context context;
    private LayoutInflater layoutInflater;

    public RecyclerAdapterFB(List<FBObject> dataset, Context context) {
        this.dataset = dataset;
        this.context = context;

        this.layoutInflater = LayoutInflater.from(context);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = layoutInflater.inflate(R.layout.fb_single_post, parent, false);
        return new MyViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        FBObject curPost = dataset.get(position);

        holder.poster.setText(curPost.getCreator()+" on "+String.valueOf(curPost.getTime()));
        holder.post.setText(curPost.getStory()+"\n"+curPost.getMessage());

        if (curPost.getPicture() != null && !curPost.getPicture().isEmpty()) {

            holder.image.setVisibility(View.VISIBLE);
            Picasso.with(context).load(curPost.getPicture())
                    .error(ic_launcher)
                    .placeholder(ic_launcher)
                    .into(holder.image);
        } else {
            holder.image.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return (dataset != null ? dataset.size() : 0);
    }

    public void loadNewFbPosts(List<FBObject> feed) {

        dataset = feed;
        notifyDataSetChanged();
    }
}
