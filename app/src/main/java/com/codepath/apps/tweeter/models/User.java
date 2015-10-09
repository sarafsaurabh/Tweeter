package com.codepath.apps.tweeter.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class User implements Parcelable {

    public String name;
    public long uid;
    public String screenName;
    public String profileImageUrl;
    public String tagLine;
    public int followersCount;
    public int followingsCount;
    public String profileBannerUrl;

    public static User fromJSON(JSONObject json) throws JSONException {
        User u = new User();
        u.name = json.getString("name");
        u.uid = json.getLong("id");
        u.screenName = json.getString("screen_name");
        u.profileImageUrl = json.getString("profile_image_url");
        u.tagLine = json.getString("description");
        u.followersCount = json.getInt("followers_count");
        u.followingsCount = json.getInt("friends_count");
        u.profileBannerUrl = json.getString("profile_banner_url");
        return u;
    }

    public User() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeLong(this.uid);
        dest.writeString(this.screenName);
        dest.writeString(this.profileImageUrl);
        dest.writeString(this.tagLine);
        dest.writeInt(this.followersCount);
        dest.writeInt(this.followingsCount);
        dest.writeString(this.profileBannerUrl);
    }

    protected User(Parcel in) {
        this.name = in.readString();
        this.uid = in.readLong();
        this.screenName = in.readString();
        this.profileImageUrl = in.readString();
        this.tagLine = in.readString();
        this.followersCount = in.readInt();
        this.followingsCount = in.readInt();
        this.profileBannerUrl = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
