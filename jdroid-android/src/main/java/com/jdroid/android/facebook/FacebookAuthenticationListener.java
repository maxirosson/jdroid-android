package com.jdroid.android.facebook;

public interface FacebookAuthenticationListener {
	
	/**
	 * Called when the login with facebook completes.
	 * 
	 * @param facebookConnector the {@link FacebookConnector} containing with the active session.
	 */
	public void onFacebookLoginCompleted(FacebookConnector facebookConnector);
	
	public void onFacebookLogoutCompleted();
	
}