package com.jdroid.android.twitter;

import android.content.Context;
import android.util.Log;

import com.jdroid.android.BuildConfig;
import com.jdroid.android.lifecycle.ApplicationLifecycleCallback;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.utils.LoggerUtils;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;

import org.slf4j.Logger;

public class TwitterAppLifecycleCallback extends ApplicationLifecycleCallback {

	private final static Logger LOGGER = LoggerUtils.getLogger(TwitterAppLifecycleCallback.class);

	@Override
	public void onCreate(final Context context) {
		ExecutorUtils.execute(new Runnable() {
			@Override
			public void run() {
				String twitterOauthConsumerKey = TwitterAppContext.getTwitterOauthConsumerKey();
				String twitterOauthConsumerSecret = TwitterAppContext.getTwitterOauthConsumerSecret();
				if (twitterOauthConsumerKey == null || twitterOauthConsumerSecret == null) {
					LOGGER.error("Missing TWITTER_OAUTH_CONSUMER_KEY or TWITTER_OAUTH_CONSUMER_SECRET");
				} else {
					TwitterAuthConfig authConfig = new TwitterAuthConfig(twitterOauthConsumerKey, twitterOauthConsumerSecret);
					TwitterConfig.Builder builder = new TwitterConfig.Builder(context);
					builder.logger(new DefaultLogger(Log.DEBUG));
					builder.twitterAuthConfig(authConfig);
					builder.debug(BuildConfig.DEBUG);
					builder.build();
					Twitter.initialize(builder.build());

					// Get TwitterCore instance on the worker thread to avoid reading the shared preferences on UI thread
					TwitterCore.getInstance();
				}
			}
		});
	}
}
