package com.jdroid.android.social.googleplus;

import com.jdroid.android.usecase.DefaultAbstractUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public abstract class GooglePlusAuthenticationUseCase extends DefaultAbstractUseCase {
	
	private String googleUserId;
	private Boolean loginMode = true;
	
	/**
	 * @see com.jdroid.android.usecase.AbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		if (loginMode) {
			onConnectToGooglePlus(googleUserId);
		} else {
			onDisconnectFromGooglePlus();
		}
	}
	
	protected abstract void onConnectToGooglePlus(String googleUserId);
	
	protected abstract void onDisconnectFromGooglePlus();
	
	/**
	 * @param loginMode the loginMode to set
	 */
	public void setLoginMode(Boolean loginMode) {
		this.loginMode = loginMode;
	}
	
	/**
	 * @param googleUserId the googleUserId to set
	 */
	public void setGoogleUserId(String googleUserId) {
		this.googleUserId = googleUserId;
	}
	
	public Boolean isLoginMode() {
		return loginMode;
	}
	
	/**
	 * @return the googleUserId
	 */
	public String getGoogleUserId() {
		return googleUserId;
	}
}
