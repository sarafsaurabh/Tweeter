package com.codepath.apps.tweeter.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codepath.apps.tweeter.R;
import com.codepath.apps.tweeter.models.Media;
import com.codepath.apps.tweeter.models.Tweet;
import com.squareup.picasso.Picasso;

public class TweetDetailActivity extends AppCompatActivity {

    private Tweet tweet;
    private ImageView ivProfile;
    private TextView tvUsername;
    private TextView tvScreenName;
    private TextView tvTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);
        setActionBar();

        tweet = getIntent().getParcelableExtra("tweet");

        ivProfile = (ImageView) findViewById(R.id.ivProfile);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        tvTweet = (TextView) findViewById(R.id.tvTweet);

        tvUsername.setText(tweet.user.name);
        tvScreenName.setText("@" + tweet.user.screenName);
        Picasso.with(getApplicationContext())
                .load(tweet.user.profileImageUrl.replace("normal", "bigger")).into(ivProfile);
        tvTweet.setText(tweet.body);

        if(tweet.medias != null && tweet.medias.size() > 0) {
            addMedias();
        }

    }

    private void addMedias() {
        for(Media m: tweet.medias) {
            ImageView iv = new ImageView(this);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            Picasso.with(getApplicationContext())
                    .load(m.mediaUrl).into(iv);
            RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlMedia);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            rl.addView(iv, lp);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tweet_detail, menu);
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
        getSupportActionBar().setLogo(R.drawable.ic_twitter);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setElevation(0);
    }
}
