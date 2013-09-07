package com.jdroid.android.social.facebook;

import com.facebook.SessionState;

public interface SessionStateListener {
	
	/**
	 * Callback related to {@link SessionState#OPENED}
	 */
	public void onSessionOpened();
	
	/**
	 * Callback related to {@link SessionState#CLOSED}
	 */
	public void onSessionClosed();
	
	/**
	 * Callback related to {@link SessionState#OPENED_TOKEN_UPDATED}
	 */
	public void onSessionOpenedWithUpdatedToken();
	
	/**
	 * Callback related to {@link SessionState#CLOSED_LOGIN_FAILED}
	 */
	public void onSessionClosedLoginFailed();
}