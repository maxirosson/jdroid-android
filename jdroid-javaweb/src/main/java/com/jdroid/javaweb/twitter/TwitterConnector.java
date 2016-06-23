package com.jdroid.javaweb.twitter;

import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.javaweb.context.Application;

import org.slf4j.Logger;

import java.util.List;

import twitter4j.Query;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterConnector {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(TwitterConnector.class);
	
	public static final Integer CHARACTERS_LIMIT = 140;
	public static final Integer URL_CHARACTERS_COUNT = 22;
	
	private TwitterFactory twitterFactory;
	
	public TwitterConnector() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true);
		cb.setOAuthConsumerKey(Application.get().getAppContext().getTwitterOAuthConsumerKey());
		cb.setOAuthConsumerSecret(Application.get().getAppContext().getTwitterOAuthConsumerSecret());
		cb.setOAuthAccessToken(Application.get().getAppContext().getTwitterOAuthAccessToken());
		cb.setOAuthAccessTokenSecret(Application.get().getAppContext().getTwitterOAuthAccessTokenSecret());
		twitterFactory = new TwitterFactory(cb.build());
	}
	
	public void tweetSafe(String text) {
		try {
			if (Application.get().getAppContext().isTwitterEnabled()) {
				Status status = twitterFactory.getInstance().updateStatus(text);
				LOGGER.info("Successfully updated the status to [" + status.getText() + "].");
			} else {
				LOGGER.info("Ignored tweet status [" + text + "].");
			}
		} catch (TwitterException e) {
			LOGGER.error("Error when posting on Twitter: [" + text + "]", e);
		}
	}
	
	public List<Status> searchTweets(String queryText) {
		Twitter twitter = twitterFactory.getInstance();
		Query query = new Query(queryText);
		try {
			return twitter.search(query).getTweets();
		} catch (TwitterException e) {
			throw new UnexpectedException(e);
		}
	}
	
}
