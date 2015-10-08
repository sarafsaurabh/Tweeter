package com.codepath.apps.tweeter.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.tweeter.R;
import com.codepath.apps.tweeter.TweeterApp;
import com.codepath.apps.tweeter.models.Tweet;
import com.codepath.apps.tweeter.models.User;
import com.codepath.apps.tweeter.util.NetworkUtil;
import com.codepath.apps.tweeter.util.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class TweetActivity extends AppCompatActivity {

    private ImageView ivProfile;
    private TextView tvUsername;
    private TextView tvScreenName;
    private EditText etTweet;
    private TwitterClient client;
    private User user;
    private MenuItem miCharCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);
        setActionBar();

        ivProfile = (ImageView) findViewById(R.id.ivProfile);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        etTweet = (EditText) findViewById(R.id.etTweet);
        client = TweeterApp.getRestClient();
        loadUserInformation();
    }

    private void loadUserInformation() {
        client.getUserInfo(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                Log.d(getClass().toString(), json.toString());
                try {
                    user = User.fromJSON(json);
                    tvUsername.setText(user.name);
                    tvScreenName.setText("@" + user.screenName);
                    Picasso.with(getApplicationContext())
                            .load(user.profileImageUrl.replace("normal", "bigger")).into(ivProfile);

                } catch (JSONException e) {
                    Log.e(getClass().toString(), e.getMessage());
                    Toast.makeText(getApplicationContext(),
                            "Not able to load user information", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, JSONObject errorResponse) {
                Log.e(getClass().toString(), errorResponse.toString());
                Toast.makeText(getApplicationContext(),
                        "Not able to load user information", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tweet, menu);

        miCharCount = menu.findItem(R.id.miCharCount);
        miCharCount.setEnabled(false);

        etTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int tweetLength = etTweet.getText().length();
                miCharCount.setTitle(String.valueOf(140 - tweetLength));
                if (tweetLength > 140) {
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.miTweet:
                onTweet(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void setActionBar() {
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF4A9CED));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_twitter);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle("Tweet");
        getSupportActionBar().setElevation(0);
    }

    public void onTweet(MenuItem item) {

        if(TextUtils.isEmpty(etTweet.getText().toString())) {
            Toast.makeText(getApplicationContext(),
                    "Please enter a tweet", Toast.LENGTH_SHORT).show();
            return;
        }
        if(etTweet.getText().length() > 140) {
            Toast.makeText(getApplicationContext(),
                    "Please enter a valid tweet", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!NetworkUtil.isNetworkAvailable(this)) {
            Toast.makeText(getApplicationContext(),
                    "No internet connection available", Toast.LENGTH_SHORT).show();
            finish();
        }
        client.postTweet(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                Log.d(getClass().toString(), json.toString());
                try {
                    Tweet tweet = Tweet.fromJSON(json);
                    Intent intent = new Intent();
                    intent.putExtra("tweet", tweet);
                    setResult(RESULT_OK, intent);
                    finish();

                } catch (JSONException e) {
                    Log.e(getClass().toString(), e.getMessage());
                    Toast.makeText(getApplicationContext(),
                            "Not able to post tweet", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response,
                                  Throwable throwable) {
                Log.e(getClass().toString(), response);
                Toast.makeText(getApplicationContext(),
                        "Not able to post tweet", Toast.LENGTH_SHORT).show();
            }
        }, etTweet.getText().toString());
    }

    private Tweet getMockTweet() {
        Tweet tweet = new Tweet();
        tweet.body = etTweet.getText().toString();
        tweet.user = new User();
        tweet.user.name = "test";
        tweet.user.profileImageUrl= "http://pbs.twimg.com/profile_images/629772340709191680//X1TGvzif_bigger.jpg";
        tweet.user.screenName = "test";
        tweet.user.uid = 2l;
        tweet.createdAt = "Sat Oct 03 07:06:37 +0000 2015";

        return tweet;
    }
}
