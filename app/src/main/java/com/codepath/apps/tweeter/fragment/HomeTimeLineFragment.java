package com.codepath.apps.tweeter.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.tweeter.R;
import com.codepath.apps.tweeter.TweeterApp;
import com.codepath.apps.tweeter.models.Tweet;
import com.codepath.apps.tweeter.util.NetworkUtil;
import com.codepath.apps.tweeter.util.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;

import java.util.List;

public class HomeTimeLineFragment extends TweetsListFragment {

    private TwitterClient client;
    private static final int TWITTER_FETCH_COUNT = 25;
    MenuItem miActionProgressItem;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        client = TweeterApp.getRestClient();
        populateTimeline(1L, null, false);
    }

    protected void populateTimeline(
            Long sinceId, Long maxId, final boolean appendToFront) {

        if(!NetworkUtil.isNetworkAvailable(getContext())) {
            Toast.makeText(
                    getContext(), "No internet connection available", Toast.LENGTH_SHORT).show();
            return;
        }

        showProgressBar();

        client.getHomeTimeLine(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                hideProgressBar();
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
                hideProgressBar();
                Log.e(getClass().toString(), errorResponse.toString());
            }
        }, sinceId, maxId, TWITTER_FETCH_COUNT);
    }

    public void showProgressBar() {
        miActionProgressItem.setVisible(true);
    }

    public void hideProgressBar() {
        miActionProgressItem.setVisible(false);
    }
}
