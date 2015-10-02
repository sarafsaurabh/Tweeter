package com.codepath.apps.tweeter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.codepath.apps.tweeter.R;
import com.codepath.apps.tweeter.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    private static class ViewHolder {
        private ImageView ivProfile;
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

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Tweet tweet = getItem(position);

        viewHolder.ivProfile.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(tweet.user.profileImageUrl
                .replace("normal", "bigger")).into(viewHolder.ivProfile);

        return convertView;
    }
}
