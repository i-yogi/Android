package com.yogidevelopers.yogiandroid.socialfusion.ver_10_22.fcbk.recyclerView;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by yogi on 9/12/16.
 */
public class FBObject implements Parcelable {

    private String id;
    private String creator;
    private Date time;
    private String story;
    private String message;
    private String picture;
    private String url;


    public FBObject(String id, String creator, Date time,
                    String story, String message,
                    String picture, String url) {
        this.id = id;
        this.creator = creator;
        this.time = time;
        this.story = story;
        this.message = message;
        this.picture = picture;
        this.url = url;
    }

    private FBObject(Parcel in) {

        id = in.readString();
        creator = in.readString();
        time = new Date(in.readLong());
        story = in.readString();
        message = in.readString();
        picture = in.readString();
        url = in.readString();
    }

    public String getId() {
        return id;
    }

    public String getCreator() {
        return creator;
    }

    public Date getTime() {
        return time;
    }

    public String getStory() {
        return story;
    }

    public String getMessage() {
        return message;
    }

    public String getPicture() {
        return picture;
    }

    public String getUrl() {
        return url;
    }


    @Override
    public String toString() {
        return "FBObject{" +
                "id='" + id + '\'' +
                "creator='" + creator + '\'' +
                ", time='" + time + '\'' +
                ", story='" + story + '\'' +
                ", message='" + message + '\'' +
                ", picture='" + picture + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);
        dest.writeString(creator);
        dest.writeLong(time.getTime());
        dest.writeString(story);
        dest.writeString(message);
        dest.writeString(picture);
        dest.writeString(url);
    }

    public static final Parcelable.Creator<FBObject> CREATOR = new Parcelable.Creator<FBObject>() {
        public FBObject createFromParcel(Parcel in) {
            return new FBObject(in);
        }

        public FBObject[] newArray(int size) {
            return new FBObject[size];
        }
    };
}
