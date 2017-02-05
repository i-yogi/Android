package com.yogidevelopers.yogiandroid.socialfusion.ver_10_22.twtr.recyclerView;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by yogi on 10/9/16.
 */

public class TweetObject implements Parcelable{

    //Strings
    private String id;
    private Date time;
    private String post;
    private String media_url;

    //users(Obj)
    private String user_name;
    private String user_id;
    private String user_img;

    public TweetObject(String id, Date time, String post,
                       String user_img, String user_id,
                       String user_name, String media_url) {
        this.id = id;
        this.time = time;
        this.post = post;
        this.user_img = user_img;
        this.user_id = user_id;
        this.user_name = user_name;
        this.media_url = media_url;
    }

    private TweetObject(Parcel in) {

        id = in.readString();
        time = new Date(in.readLong());
        post = in.readString();
        media_url = in.readString();
        user_name = in.readString();
        user_id = in.readString();
        user_img = in.readString();
    }

    public String getId() {
        return id;
    }

    public Date getTime() {
        return time;
    }

    public String getPost() {
        return post;
    }



    public String getMedia_url() {
        return media_url;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_img() {
        return user_img;
    }

    @Override
    public String toString() {
        return "TweetObject{" +
                "id='" + id + '\'' +
                ", time='" + time + '\'' +
                ", post='" + post + '\'' +
                ", media_url='" + media_url + '\'' +
                ", user_name='" + user_name + '\'' +
                ", user_id='" + user_id + '\'' +
                ", user_img='" + user_img + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);
        dest.writeLong(time.getTime());
        dest.writeString(post);
        dest.writeString(media_url);
        dest.writeString(user_name);
        dest.writeString(user_id);
        dest.writeString(user_img);
    }

    public static final Parcelable.Creator<TweetObject> CREATOR = new Parcelable.Creator<TweetObject>() {
        public TweetObject createFromParcel(Parcel in) {
            return new TweetObject(in);
        }

        public TweetObject[] newArray(int size) {
            return new TweetObject[size];
        }
    };
}
