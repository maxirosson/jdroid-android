package com.jdroid.android.social.facebook;


/**
 * 
 * @author Maxi Rosson
 */
public interface FacebookListener {
	
	public void onFacebookConnected(FacebookUser facebookUser);
	
	public void onFacebookDisconnected();
}
