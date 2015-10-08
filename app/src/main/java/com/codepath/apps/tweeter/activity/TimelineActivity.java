package com.codepath.apps.tweeter.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.tweeter.R;
import com.codepath.apps.tweeter.fragment.HomeTimeLineFragment;
import com.codepath.apps.tweeter.models.Tweet;

public class TimelineActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_TIMELINE = 1;
    HomeTimeLineFragment homeTimeLineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        setActionBar();

        if(savedInstanceState == null) {
            homeTimeLineFragment = (HomeTimeLineFragment)
                    getSupportFragmentManager().findFragmentById(R.id.fragment_timeline);
        }
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
                homeTimeLineFragment.insertTweet(tweet, 0);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
