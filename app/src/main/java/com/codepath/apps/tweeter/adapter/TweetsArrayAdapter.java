package com.codepath.apps.tweeter.adapter;

import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.tweeter.R;
import com.codepath.apps.tweeter.models.Tweet;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    private static class ViewHolder {
        private ImageView ivProfile;
        public TextView tvTweet;
        public TextView tvUsername;
        public TextView tvScreenName;
        public TextView tvCreatedTime;

    }

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

        if(convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.item_tweet, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.ivProfile = (ImageView) convertView.findViewById(R.id.ivProfile);
            viewHolder.tvScreenName = (TextView) convertView.findViewById(R.id.tvScreenName);
            viewHolder.tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
            viewHolder.tvCreatedTime = (TextView) convertView.findViewById(R.id.tvCreatedTime);
            viewHolder.tvTweet = (TextView) convertView.findViewById(R.id.tvTweet);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Tweet tweet = getItem(position);

        viewHolder.ivProfile.setImageResource(android.R.color.transparent);
        viewHolder.tvScreenName.setText("@" + tweet.user.screenName);
        viewHolder.tvUsername.setText(tweet.user.name);

        // 37 minutes ago => ["37", "minutes", "ago"]
        try {
            String[] createdTimeSplit =
                    DateUtils.getRelativeTimeSpanString(
                            getTwitterDate(tweet.createdAt).getTime(),
                            System.currentTimeMillis(),
                            DateUtils.SECOND_IN_MILLIS).toString().split(" ");

            viewHolder.tvCreatedTime.setText(createdTimeSplit[0] + createdTimeSplit[1].charAt(0));
        } catch (ParseException e) {
            Log.d(getClass().toString(), e.getMessage());
        }

        viewHolder.tvTweet.setText(Html.fromHtml(tweet.body));

        Picasso.with(getContext()).load(tweet.user.profileImageUrl
                .replace("normal", "bigger")).into(viewHolder.ivProfile);

        return convertView;
    }

    public static Date getTwitterDate(String date) throws ParseException {

        final String format = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(format);
        sf.setLenient(true);
        return sf.parse(date);
    }
}
