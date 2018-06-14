package com.jdroid.android.twitter;

import android.support.annotation.MainThread;
import android.view.ViewGroup;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.http.exception.ConnectionException;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiException;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TimelineResult;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.List;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

// TODO Add support to rotation. The helper is called on each screen rotation
public abstract class TwitterHelper {
	
	private ViewGroup tweetContainer;
	
	private SearchTimeline.Builder searchTimelineBuilder;
	private List<Tweet> tweets = Lists.newArrayList();
	
	private TwitterQuery twitterQuery;
	
	private Boolean pendingSuccessNotification = false;
	private Boolean pendingFailureNotification = false;
	
	protected SearchTimeline createSearchTimeline() {
		if (twitterQuery != null) {
			searchTimelineBuilder = new SearchTimeline.Builder();
			searchTimelineBuilder.languageCode(twitterQuery.getLanguageCode());
			searchTimelineBuilder.maxItemsPerRequest(twitterQuery.getMaxItemsPerRequest());
			searchTimelineBuilder.query(twitterQuery.getQuery());
		}
		return searchTimelineBuilder.build();
	}
	
	public void loadTweets() {
		onStartLoadingTweets();
		
		try {
			SearchTimeline searchTimeline = createSearchTimeline();
			searchTimeline.next(null, new Callback<TimelineResult<Tweet>>() {
				@Override
				public void success(Result<TimelineResult<Tweet>> result) {
					try {
						tweets.clear();
						for(Tweet each : result.data.items) {
							if (isValidTweet(each)) {
								tweets.add(each);
							}
						}
						notifySuccess();
					} catch (Exception e) {
						AbstractApplication.get().getExceptionHandler().logHandledException(e);
					}
				}
				
				@Override
				public void failure(TwitterException e) {
					Boolean connectionError = false;
					if (e instanceof TwitterApiException) {
						TwitterApiException twitterApiException = (TwitterApiException)e;
						if ("Unable to resolve host \"api.twitter.com\": No address associated with hostname".equals(e.getMessage())) {
							connectionError = true;
						} else if (e.getMessage() != null && e.getMessage().startsWith("failed to connect to ")) {
							connectionError = true;
						} else {
							String errorMessage = twitterApiException.getErrorCode() + " " + twitterApiException.getErrorMessage();
							AbstractApplication.get().getCoreAnalyticsSender().trackErrorLog(errorMessage);
						}
					} else if ("Request Failure".equals(e.getMessage())) {
						if (e.getCause() != null) {
							if (e.getCause() instanceof ConnectException) {
								connectionError = true;
							} else if (e.getCause() instanceof SocketTimeoutException) {
								connectionError = true;
							} else if (e.getCause() instanceof SocketException) {
								connectionError = true;
							} else if (e.getCause() instanceof SSLHandshakeException) {
								connectionError = true;
							} else if (e.getCause() instanceof SSLException) {
								connectionError = true;
							} else if (e.getCause() instanceof IOException) {
								connectionError = true;
							} else if (e.getCause().getMessage() != null) {
								if (e.getCause().getMessage().startsWith("Failed to connect to api.twitter.com")) {
									connectionError = true;
								} else if (e.getCause().getMessage().equals("Unable to resolve host \"api.twitter.com\": No address associated with hostname")) {
									connectionError = true;
								}
							}
						}
					}
					if (connectionError) {
						AbstractApplication.get().getExceptionHandler().logHandledException(new ConnectionException(e));
					} else {
						AbstractApplication.get().getExceptionHandler().logHandledException(e);
					}
					notifyFailure();
				}
			});
		} catch (Exception e) {
			notifyFailure();
			AbstractApplication.get().getExceptionHandler().logHandledException(e);
		}
	}
	
	private void notifySuccess() {
		if (getAbstractFragment() != null && getAbstractFragment().isResumed()) {
			pendingSuccessNotification = false;
			TwitterHelper.this.onSuccess(tweets);
		} else {
			pendingSuccessNotification = true;
		}
	}
	
	private void notifyFailure() {
		if (getAbstractFragment() != null && getAbstractFragment().isResumed()) {
			pendingFailureNotification = false;
			TwitterHelper.this.onFailure();
		} else {
			pendingFailureNotification = true;
		}
	}

	@MainThread
	protected  void onStartLoadingTweets() {
		// Do Nothing
	}

	@MainThread
	protected abstract void onSuccess(List<Tweet> tweets);

	@MainThread
	protected void onFailure() {
		// Do Nothing
	}

	protected abstract AbstractFragment getAbstractFragment();
	
	protected Boolean isValidTweet(Tweet tweet) {
		return true;
	}
	
	public void setSearchTimelineBuilder(SearchTimeline.Builder searchTimelineBuilder) {
		this.searchTimelineBuilder = searchTimelineBuilder;
	}
	
	public List<Tweet> getTweets() {
		return tweets;
	}
	
	public void setTweetContainer(ViewGroup tweetContainer) {
		this.tweetContainer = tweetContainer;
	}
	
	public ViewGroup getTweetContainer() {
		return tweetContainer;
	}
	
	public void setTwitterQuery(TwitterQuery twitterQuery) {
		this.twitterQuery = twitterQuery;
	}
	
	public void onResume() {
		
		if (pendingSuccessNotification) {
			notifySuccess();
		}
		
		if (pendingFailureNotification) {
			notifyFailure();
		}
	}
}
