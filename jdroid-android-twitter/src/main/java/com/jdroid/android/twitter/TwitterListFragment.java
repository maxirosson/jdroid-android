package com.jdroid.android.twitter;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;

import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.loading.FragmentLoading;
import com.jdroid.android.loading.SwipeRefreshLoading;

// TODO Add no results (or error) support
public abstract class TwitterListFragment extends AbstractFragment implements SwipeRefreshLayout.OnRefreshListener {
	
	private TwitterHelper twitterHelper;
	
	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.jdroid_twitter_list_fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		twitterHelper = createTwitterHelper();
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		twitterHelper.setTweetContainer((ViewGroup)findView(R.id.tweetContainer));
		if (loadTweetsOnViewCreated()) {
			twitterHelper.loadTweets();
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		twitterHelper.onResume();
	}
	
	@Override
	public void onRefresh() {
		twitterHelper.loadTweets();
	}
	
	@Override
	public FragmentLoading getDefaultLoading() {
		return new SwipeRefreshLoading();
	}
	
	protected Boolean loadTweetsOnViewCreated() {
		return true;
	}
	
	protected TwitterHelper getTwitterHelper() {
		return twitterHelper;
	}
	
	protected abstract TwitterHelper createTwitterHelper();
}
