package com.codepath.apps.tweeter.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.tweeter.R;
import com.codepath.apps.tweeter.TweeterApp;
import com.codepath.apps.tweeter.fragment.UserTimelineFragment;
import com.codepath.apps.tweeter.models.User;
import com.codepath.apps.tweeter.util.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {

    TwitterClient client;
    User user;
    ImageView ivProfileBkg;
    TextView tvTweets;
    TextView tvFollowers;
    TextView tvFollowing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setActionBar();

        ivProfileBkg = (ImageView) findViewById(R.id.ivProfileBkg);
        tvTweets = (TextView) findViewById(R.id.tvTweets);
        tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        tvFollowing = (TextView) findViewById(R.id.tvFollowing);

        client = TweeterApp.getRestClient();

        JsonHttpResponseHandler j = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess ( int statusCode, Header[] headers, JSONObject response){
                try {
                    Log.d(getClass().getName(), response.toString());
                    user = User.fromJSON(response);
                    getSupportActionBar().setDisplayShowTitleEnabled(true);
                    getSupportActionBar().setTitle("@" + user.screenName);

                    if (user.profileBannerUrl != null) {
                        Picasso.with(
                                getApplicationContext()).load(
                                user.profileBannerUrl).into(ivProfileBkg);
                    }

                    String styledTweetText = "<font color=\"#000000\">"
                            + String.valueOf(user.tweetCount) + "</font>" + "<br/>" + "TWEETS";
                    tvTweets.setText(Html.fromHtml(styledTweetText),
                            TextView.BufferType.SPANNABLE);

                    String styledTweetFollowers = "<font color=\"#000000\">"
                            + String.valueOf(user.followersCount) + "</font>" + "<br/>" + "FOLLOWERS";
                    tvFollowers.setText(Html.fromHtml(styledTweetFollowers),
                            TextView.BufferType.SPANNABLE);

                    String styledTweetFollowing = "<font color=\"#000000\">"
                            + String.valueOf(user.followingsCount) + "</font>" + "<br/>" + "FOLLOWING";
                    tvFollowing.setText(Html.fromHtml(styledTweetFollowing),
                            TextView.BufferType.SPANNABLE);

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),
                            "Not able to load user information", Toast.LENGTH_SHORT).show();
                }
            }
        };

        String screenName = getIntent().getStringExtra("screen_name");

        if(screenName == null) {
            client.getUserInfo(j);
        } else {
            client.getUserInfo(screenName, j);
        }

        if(savedInstanceState == null) {
            UserTimelineFragment userTimelineFragment =
                    UserTimelineFragment.newInstance(screenName);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, userTimelineFragment);
            ft.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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
}
