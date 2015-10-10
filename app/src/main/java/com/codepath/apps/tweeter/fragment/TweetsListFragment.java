package com.codepath.apps.tweeter.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.tweeter.R;
import com.codepath.apps.tweeter.adapter.TweetsArrayAdapter;
import com.codepath.apps.tweeter.models.Tweet;
import com.codepath.apps.tweeter.util.EndlessScrollListener;

import java.util.ArrayList;
import java.util.List;

public abstract class TweetsListFragment extends Fragment {

    private ArrayList<Tweet> tweets = new ArrayList<>();
    private TweetsArrayAdapter aTweets;
    private ListView lvTweets;
    private SwipeRefreshLayout scTweets;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweets, container, false);

        lvTweets = (ListView) view.findViewById(R.id.lvTweets);

        lvTweets.setAdapter(aTweets);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                Tweet lastTweet = tweets.get(tweets.size() - 1);
                // since we'll be fetching old tweets, append to the end of list
                populateTimeline(1L, lastTweet.uid - 1, false);
                return false;
            }
        });

        scTweets = (SwipeRefreshLayout) view.findViewById(R.id.scTweets);
        // Configure the refreshing colors
        scTweets.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        scTweets.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(tweets != null) {
                    Tweet firstTweet = tweets.get(0);
                    // since we'll be fetching new tweets, append to the front of list
                    populateTimeline(firstTweet.uid, null, true);
                    scTweets.setRefreshing(false);
                }
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);

        aTweets = new TweetsArrayAdapter(this.getContext(), tweets);
    }

    public void insertTweet(Tweet tweet, int pos) {
        aTweets.insert(tweet, pos);
    }

    public void addAllTweets(List<Tweet> tweets) {
        aTweets.addAll(tweets);
    }

    protected abstract void populateTimeline(
            Long sinceId, Long maxId, final boolean appendToFront);
}
