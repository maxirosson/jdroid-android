package com.jdroid.android.twitter;

import org.slf4j.Logger;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.jdroid.android.R;
import com.jdroid.android.activity.AbstractActivity;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.android.utils.ToastUtils;
import com.jdroid.java.utils.ExecutorUtils;
import com.jdroid.java.utils.LoggerUtils;

/**
 * TwitterBridgeActivity Activity
 * 
 */
public class TwitterBridgeActivity extends AbstractActivity {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(TwitterBridgeActivity.class);
	
	public static final String OAUTH_TOKEN = "FkvSasdasdA";
	public static final String SECRET_KEY = "clCvzVRK0bKALKmnN38NasdasdslgYpg0yndTb78HndwmG2K7A";
	private static final String CALLBACK_URL = "connect://" + AndroidUtils.getApplicationName();
	public static final String POST_ON_TWITTER = "postOnTwitter";
	
	private Twitter twitter;
	private RequestToken requestToken;
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#getContentView()
	 */
	@Override
	public int getContentView() {
		return R.layout.twitter_bridge_activity;
	}
	
	/**
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(OAUTH_TOKEN, SECRET_KEY);
		
		ExecutorUtils.execute(new Runnable() {
			
			@Override
			public void run() {
				try {
					requestToken = twitter.getOAuthRequestToken(CALLBACK_URL);
					String url = requestToken.getAuthorizationURL();
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					startActivity(intent);
					
				} catch (TwitterException te) {
					if (TwitterErrorType.find(te).equals(TwitterErrorType.OAUTH_ERROR)) {
						LOGGER.error("Unable to get the Twitter access token.", te);
					} else {
						LOGGER.error("Error connecting with Twitter.", te);
					}
					finish();
					ToastUtils.showToastOnUIThread(R.string.twitterError);
				}
			}
		});
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Uri uri = intent.getData();
		
		try {
			String verifier = uri.getQueryParameter("oauth_verifier");
			if (verifier != null) {
				AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
				twitter.setOAuthAccessToken(accessToken);
			} else {
				ToastUtils.showToastOnUIThread(R.string.twitterNoAuth);
				finish();
			}
			
		} catch (TwitterException te) {
			LOGGER.error("Error connecting with Twitter.", te);
			ToastUtils.showToastOnUIThread(R.string.twitterError);
			finish();
		}
	}
}
