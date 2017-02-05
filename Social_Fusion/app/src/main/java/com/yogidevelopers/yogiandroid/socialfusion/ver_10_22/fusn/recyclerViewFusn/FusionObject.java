package com.yogidevelopers.yogiandroid.socialfusion.ver_10_22.fusn.recyclerViewFusn;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by yogi on 10/13/16.
 */

public class FusionObject implements Parcelable {

    private String objType;
    private long comTime;
    private String hashTags;
    private String id;

    //FB Fields
    private String fb_creator;
    private String fb_story;
    private String fb_message;
    private String fb_picture;
    private String fb_url;
    private String tw_post;
    private String tw_media_url;
    private String tw_user_name;
    private String tw_user_id;
    private String tw_user_img;

    public String getObjType() {
        return objType;
    }

    public long getComTime() {
        return comTime;
    }

    public String getId() {
        return id;
    }

    public String getFb_story() {
        return fb_story;
    }

    public String getFb_message() {
        return fb_message;
    }

    public String getFb_picture() {
        return fb_picture;
    }

    public String getFb_url() {
        return fb_url;
    }

    public String getTw_post() {
        return tw_post;
    }

    public String getTw_media_url() {
        return tw_media_url;
    }

    public String getTw_user_name() {
        return tw_user_name;
    }

    public String getTw_user_id() {
        return tw_user_id;
    }

    public String getTw_user_img() {
        return tw_user_img;
    }

    public String getHashTags() {
        return hashTags;
    }

    public String getFb_creator() {
        return fb_creator;
    }

    public FusionObject(String objType, long comTime, String hashTags, String id, String fb_creator,
                        String fb_story, String fb_message, String fb_picture, String fb_url,
                        String tw_post, String tw_media_url, String tw_user_name, String tw_user_id,
                        String tw_user_img) {
        this.objType = objType;
        this.comTime = comTime;
        this.hashTags = hashTags;
        this.id = id;
        this.fb_creator = fb_creator;
        this.fb_story = fb_story;
        this.fb_message = fb_message;
        this.fb_picture = fb_picture;
        this.fb_url = fb_url;
        this.tw_post = tw_post;
        this.tw_media_url = tw_media_url;
        this.tw_user_name = tw_user_name;
        this.tw_user_id = tw_user_id;
        this.tw_user_img = tw_user_img;
    }

    private FusionObject(Parcel in) {

        id = in.readString();
        comTime = in.readLong();
        hashTags = in.readString();
        fb_creator = in.readString();
        fb_story = in.readString();
        fb_message = in.readString();
        fb_picture = in.readString();
        fb_url = in.readString();
        tw_post = in.readString();
        tw_media_url = in.readString();
        tw_user_name = in.readString();
        tw_user_id = in.readString();
        tw_user_img = in.readString();
    }

    @Override
    public String toString() {
        return "FusionObject{" +
                "objType='" + objType + '\'' +
                ", fb_time='" + String.valueOf(new Date(comTime)) + '\'' +
                ", hashTags='" + hashTags + '\'' +
                ", id='" + id + '\'' +
                ", id='" + fb_creator + '\'' +
                ", fb_story='" + fb_story + '\'' +
                ", fb_message='" + fb_message + '\'' +
                ", fb_picture='" + fb_picture + '\'' +
                ", fb_url='" + fb_url + '\'' +
                ", tw_post='" + tw_post + '\'' +
                ", tw_media_url='" + tw_media_url + '\'' +
                ", tw_user_name='" + tw_user_name + '\'' +
                ", tw_user_id='" + tw_user_id + '\'' +
                ", tw_user_img='" + tw_user_img + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(fb_creator);
        dest.writeString(id);
        dest.writeLong(comTime);
        dest.writeString(hashTags);
        dest.writeString(fb_story);
        dest.writeString(fb_message);
        dest.writeString(fb_picture);
        dest.writeString(fb_url);
        dest.writeString(tw_post);
        dest.writeString(tw_media_url);
        dest.writeString(tw_user_name);
        dest.writeString(tw_user_id);
        dest.writeString(tw_user_img);
    }

    public static final Parcelable.Creator<FusionObject> CREATOR = new Parcelable.Creator<FusionObject>() {
        public FusionObject createFromParcel(Parcel in) {
            return new FusionObject(in);
        }

        public FusionObject[] newArray(int size) {
            return new FusionObject[size];
        }
    };

    public static Comparator<FusionObject> PostDate = new Comparator<FusionObject>() {
        @Override
        public int compare(FusionObject lhs, FusionObject rhs) {

            Date lhsTime = new Date(lhs.getComTime());
            Date rhsTime = new Date(rhs.getComTime());

            return lhsTime.compareTo(rhsTime);
        }
    };

}
