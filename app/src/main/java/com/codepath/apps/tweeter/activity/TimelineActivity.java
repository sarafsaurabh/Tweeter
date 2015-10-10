package com.codepath.apps.tweeter.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.tweeter.R;
import com.codepath.apps.tweeter.fragment.HomeTimeLineFragment;
import com.codepath.apps.tweeter.fragment.MentionsTimelineFragment;
import com.codepath.apps.tweeter.models.Tweet;

public class TimelineActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_TIMELINE = 1;
    HomeTimeLineFragment homeTimeLineFragment = new HomeTimeLineFragment();
    MentionsTimelineFragment mentionsTimelineFragment = new MentionsTimelineFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        setActionBar();

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        viewPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));

        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabStrip.setViewPager(viewPager);
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

    public void onProfileView(MenuItem item) {
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

    public void onUsernameClick(View view) {
        TextView tvScreenName = (TextView) findViewById(R.id.tvScreenName);

        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("screen_name", tvScreenName.getText());
        startActivity(i);
    }

    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = {"Home", "Mentions"};

        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0) {
                return homeTimeLineFragment;
            } else if(position == 1) {
                return mentionsTimelineFragment;
            } else {
                return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return tabTitles.length ;
        }
    }
}
