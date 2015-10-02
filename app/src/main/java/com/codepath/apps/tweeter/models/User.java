package com.codepath.apps.tweeter.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

    public String name;
    public long uid;
    public String screenName;
    public String profileImageUrl;

    public static User fromJSON(JSONObject json) throws JSONException {
        User u = new User();
        u.name = json.getString("name");
        u.uid = json.getLong("id");
        u.screenName = json.getString("screen_name");
        u.profileImageUrl = json.getString("profile_image_url");

        return u;
    }
}
