package com.jdroid.android.social.facebook;

import org.apache.commons.lang.StringUtils;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import com.facebook.Session;
import com.facebook.Session.Builder;
import com.facebook.SessionState;
import com.jdroid.android.utils.AndroidEncryptionUtils;
import com.jdroid.android.utils.SharedPreferencesUtils;

public class FacebookPreferencesUtils {
	
	private static final String PREFS_FACEBOOK_ACCESS_TOKEN = "facebook.accessToken";
	private static final String PREFS_FACEBOOK_USER_ID = "facebook.userId";
	private static final String PREFS_FIRST_NAME = "facebook.firstName";
	private static final String PREFS_LAST_NAME = "facebook.lastName";
	private static final String PREFS_USER_EMAIL = "facebook.userEmail";
	
	private static Boolean existsFacebookAccessToken = false;
	
	/**
	 * @return Whether there is a Facebook Access Token stored or not
	 */
	public static Boolean existsFacebookAccessToken() {
		return existsFacebookAccessToken;
	}
	
	public static String loadFacebookAccessTokenHashFromPreferences() {
		return SharedPreferencesUtils.loadPreference(PREFS_FACEBOOK_ACCESS_TOKEN);
	}
	
	public static Boolean verifyFacebookAccesToken() {
		existsFacebookAccessToken = FacebookPreferencesUtils.loadFacebookAccessTokenHashFromPreferences() != null;
		return existsFacebookAccessToken;
	}
	
	public static FacebookUser loadFacebookUser() {
		FacebookUser facebookUserInfo = null;
		String firstName = SharedPreferencesUtils.loadPreference(PREFS_FIRST_NAME);
		String lastName = SharedPreferencesUtils.loadPreference(PREFS_LAST_NAME);
		String email = SharedPreferencesUtils.loadPreference(PREFS_USER_EMAIL);
		String facebookId = SharedPreferencesUtils.loadPreference(PREFS_FACEBOOK_USER_ID);
		
		if (StringUtils.isNotBlank(firstName) && StringUtils.isNotBlank(lastName) && StringUtils.isNotBlank(facebookId)) {
			facebookUserInfo = new FacebookUser();
			facebookUserInfo.setFirstName(firstName);
			facebookUserInfo.setEmail(email);
			facebookUserInfo.setLastName(lastName);
			facebookUserInfo.setFacebookId(facebookId);
		}
		verifyFacebookAccesToken();
		return facebookUserInfo;
	}
	
	public static void saveFacebookUser(String accessToken, FacebookUser facebookUser) {
		Editor editor = SharedPreferencesUtils.getEditor();
		editor.putString(PREFS_FACEBOOK_ACCESS_TOKEN, AndroidEncryptionUtils.generateShaHash(accessToken));
		editor.putString(PREFS_FIRST_NAME, facebookUser.getFirstName());
		editor.putString(PREFS_LAST_NAME, facebookUser.getLasttName());
		editor.putString(PREFS_USER_EMAIL, facebookUser.getEmail());
		editor.putString(PREFS_FACEBOOK_USER_ID, facebookUser.getFacebookId());
		editor.commit();
		FacebookPreferencesUtils.existsFacebookAccessToken = true;
	}
	
	public static void cleanFacebookUser() {
		SharedPreferencesUtils.removePreferences(PREFS_FACEBOOK_ACCESS_TOKEN, PREFS_FACEBOOK_USER_ID, PREFS_USER_EMAIL);
		FacebookPreferencesUtils.existsFacebookAccessToken = false;
	}
	
	public static Session openFacebookActiveSession(Context context, String applicationId) {
		Builder builder = new Builder(context);
		builder.setApplicationId(applicationId);
		Session session = builder.build();
		if (SessionState.CREATED_TOKEN_LOADED.equals(session.getState())) {
			Session.setActiveSession(session);
			session.openForRead(null);
			return session;
		}
		return null;
	}
}
