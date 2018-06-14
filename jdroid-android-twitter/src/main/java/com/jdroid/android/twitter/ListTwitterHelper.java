package com.jdroid.android.twitter;

import android.content.Context;
import android.support.annotation.MainThread;

import com.jdroid.android.fragment.AbstractFragment;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.BaseTweetView;
import com.twitter.sdk.android.tweetui.CompactTweetView;

import java.util.List;

public abstract class ListTwitterHelper extends TwitterHelper {

	@Override
	protected void onStartLoadingTweets() {
		AbstractFragment fragment = getAbstractFragment();
		if (fragment != null) {
			fragment.showLoading();
		}
	}
	
	@MainThread
	@Override
	protected void onSuccess(List<Tweet> tweets) {
		AbstractFragment fragment = getAbstractFragment();
		if (fragment != null) {
			getTweetContainer().removeAllViews();
			for(Tweet each : tweets) {
				getTweetContainer().addView(createTweetView(fragment.getContext(), each));
			}
			fragment.dismissLoading();
		}
	}
	
	protected BaseTweetView createTweetView(Context context, Tweet tweet) {
		return new CompactTweetView(context, tweet);
	}
	
	@MainThread
	@Override
	protected void onFailure() {
		AbstractFragment fragment = getAbstractFragment();
		if (fragment != null) {
			fragment.dismissLoading();
		}
	}
}
