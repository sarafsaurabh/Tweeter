package com.codepath.apps.tweeter;

import android.content.Context;

import com.codepath.apps.tweeter.util.TwitterClient;

/*
 * This is the Android application itself and is used to configure various settings
 * including the image cache in memory and on disk. This also adds a singleton
 * for accessing the relevant rest client.
 *
 *     TwitterClient client = TweeterApp.getRestClient();
 *     // use client to send requests to API
 *
 */
public class TweeterApp extends com.activeandroid.app.Application {
	private static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		TweeterApp.context = this;
	}

	public static TwitterClient getRestClient() {
		return (TwitterClient) TwitterClient.getInstance(TwitterClient.class, TweeterApp.context);
	}
}