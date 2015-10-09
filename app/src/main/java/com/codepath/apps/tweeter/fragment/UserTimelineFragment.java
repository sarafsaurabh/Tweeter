package com.codepath.apps.tweeter.fragment;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.tweeter.TweeterApp;
import com.codepath.apps.tweeter.models.Tweet;
import com.codepath.apps.tweeter.util.NetworkUtil;
import com.codepath.apps.tweeter.util.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;

import java.util.List;

public class UserTimelineFragment extends TweetsListFragment {

    private TwitterClient client;
    private static final int TWITTER_FETCH_COUNT = 25;

    public static UserTimelineFragment newInstance(String screenName) {
        UserTimelineFragment userTimelineFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        userTimelineFragment.setArguments(args);
        return userTimelineFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TweeterApp.getRestClient();
        populateTimeline(1L, null, false);
    }

    protected void populateTimeline(
            Long sinceId, Long maxId, final boolean appendToFront) {

        String screenName = getArguments().getString("screen_name");

        if (!NetworkUtil.isNetworkAvailable(getContext())) {
            Toast.makeText(
                    getContext(), "No internet connection available", Toast.LENGTH_SHORT).show();
        }
        client.getUserTimeLine(screenName, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                Log.d(getClass().toString(), json.toString());
                if (appendToFront) {
                    List<Tweet> newTweets = Tweet.fromJSONArray(json);
                    for (int i = 0; i < newTweets.size(); i++) {
                        insertTweet(newTweets.get(i), i);
                    }

                } else {
                    addAllTweets(Tweet.fromJSONArray(json));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, JSONArray errorResponse) {
                Toast.makeText(getContext(),
                        "Not able to load timeline", Toast.LENGTH_SHORT).show();
                Log.e(getClass().toString(), errorResponse.toString());
            }
        }, sinceId, maxId, TWITTER_FETCH_COUNT);
    }
}
