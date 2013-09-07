package com.jdroid.android.social.facebook;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import com.facebook.model.GraphUser;
import com.jdroid.android.usecase.DefaultAbstractUseCase;
import com.jdroid.android.utils.AndroidEncryptionUtils;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.utils.LoggerUtils;

public class FacebookAuthenticationUseCase extends DefaultAbstractUseCase {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(FacebookAuthenticationUseCase.class);
	
	private FacebookConnector facebookConnector;
	private FacebookUser facebookUser;
	private Boolean loginMode = true;
	
	/**
	 * @see com.despegar.commons.android.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		
		if (loginMode) {
			facebookUser = getFacebookUserFromCache();
			if (facebookUser == null) {
				
				GraphUser fbUser = facebookConnector.executeMeRequest();
				String accessToken = facebookConnector.getAccessToken();
				
				if (fbUser == null) {
					throw new UnexpectedException("Failed to get GraphUser from Facebook");
				}
				
				sendFacebookLogin(fbUser.getId(), accessToken);
				
				facebookUser = new FacebookUser();
				facebookUser.setFirstName(fbUser.getFirstName());
				facebookUser.setLastName(fbUser.getLastName());
				Object email = fbUser.asMap().get("email");
				if (email != null) {
					facebookUser.setEmail(email.toString());
				}
				
				FacebookPreferencesUtils.saveFacebookUser(accessToken, facebookUser);
			} else {
				LOGGER.debug("facebookUserInfo from cache facebookUserInfo= " + facebookUser);
			}
			afterFacebookLogin();
		} else {
			FacebookPreferencesUtils.cleanFacebookUser();
			afterFacebookLogout();
		}
	}
	
	protected void sendFacebookLogin(String facebookId, String token) {
		// Do Nothing
	}
	
	protected void afterFacebookLogin() {
		// Do Nothing
	}
	
	protected void afterFacebookLogout() {
		// Do Nothing
	}
	
	public void setFacebookConnector(FacebookConnector facebookConnector) {
		this.facebookConnector = facebookConnector;
	}
	
	public FacebookUser getFacebookUser() {
		return facebookUser;
	}
	
	private FacebookUser getFacebookUserFromCache() {
		FacebookUser facebookUser = null;
		String accessToken = facebookConnector.getAccessToken();
		if ((accessToken != null) && StringUtils.isNotBlank(accessToken)) {
			String accessTokenHash = AndroidEncryptionUtils.generateShaHash(accessToken);
			LOGGER.debug(" accessTokenHash=" + accessTokenHash);
			String savedAccessTokenHash = FacebookPreferencesUtils.loadFacebookAccessTokenHashFromPreferences();
			if (accessTokenHash.equals(savedAccessTokenHash)) {
				facebookUser = FacebookPreferencesUtils.loadFacebookUser();
			}
		}
		return facebookUser;
	}
	
	public void setLoginMode(Boolean loginMode) {
		this.loginMode = loginMode;
	}
	
	public Boolean isLoginMode() {
		return loginMode;
	}
	
}
