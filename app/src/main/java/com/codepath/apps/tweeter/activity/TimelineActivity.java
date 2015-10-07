package com.codepath.apps.tweeter.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.tweeter.R;
import com.codepath.apps.tweeter.TweeterApp;
import com.codepath.apps.tweeter.adapter.TweetsArrayAdapter;
import com.codepath.apps.tweeter.models.Tweet;
import com.codepath.apps.tweeter.util.EndlessScrollListener;
import com.codepath.apps.tweeter.util.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;
    private ArrayList<Tweet> tweets = new ArrayList<>();
    private TweetsArrayAdapter aTweets;
    private ListView lvTweets;
    private static final int REQUEST_CODE_TIMELINE = 1;
    private static final int TWITTER_FETCH_COUNT = 25;
    private SwipeRefreshLayout scTweets;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        setActionBar();

        lvTweets = (ListView) findViewById(R.id.lvTweets);
        aTweets = new TweetsArrayAdapter(this, tweets);
        lvTweets.setAdapter(aTweets);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                Tweet lastTweet = tweets.get(tweets.size() - 1);
                // since we'll be fetching old tweets, append to the end of list
                populateTimeline(1L, lastTweet.uid - 1, TWITTER_FETCH_COUNT, false);
                return false;
            }
        });

        scTweets = (SwipeRefreshLayout) findViewById(R.id.scTweets);
        // Configure the refreshing colors
        scTweets.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        scTweets.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Tweet firstTweet = tweets.get(0);
                // since we'll be fetching new tweets, append to the front of list
                populateTimeline(firstTweet.uid, null, TWITTER_FETCH_COUNT, true);
                scTweets.setRefreshing(false);
            }
        });

        client = TweeterApp.getRestClient();
        populateTimeline(1L, null, TWITTER_FETCH_COUNT, false);
    }

    private void populateTimeline(
            Long sinceId, Long maxId, int count, final boolean appendToFront) {

        if(!isNetworkAvailable()) {
            Toast.makeText(getApplicationContext(),
                    "No internet connection available", Toast.LENGTH_SHORT).show();
        }
        client.getHomeTimeLine(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                Log.d(getClass().toString(), json.toString());
                if(appendToFront) {
                    List<Tweet> newTweets = Tweet.fromJSONArray(json);
                    for (int i = 0; i < newTweets.size(); i++) {
                        aTweets.insert(newTweets.get(i), i);
                    }

                } else {
                    aTweets.addAll(Tweet.fromJSONArray(json));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, JSONArray errorResponse) {
                Toast.makeText(getApplicationContext(),
                        "Not able to load timeline", Toast.LENGTH_SHORT).show();
                Log.e(getClass().toString(), errorResponse.toString());
            }
        }, sinceId, maxId, count);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    private void setActionBar() {
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF4A9CED));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_twitter);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setElevation(0);
    }

    public void onComposeAction(MenuItem item) {
        Intent i = new Intent(this, TweetActivity.class);
        startActivityForResult(i, REQUEST_CODE_TIMELINE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_TIMELINE) {
            if(resultCode == RESULT_OK) {
                Tweet tweet = data.getParcelableExtra("tweet");
                aTweets.insert(tweet, 0);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
