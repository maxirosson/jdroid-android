package com.jdroid.android.firebase.analytics;

@Deprecated
public class FirebaseAnalyticsAppContext {

	/**
	 * @return Whether the application has Firebase Analytics enabled or not
	 */
	public static Boolean isFirebaseAnalyticsEnabled() {
		return FirebaseAnalyticsFactory.getFirebaseAnalyticsHelper().isFirebaseAnalyticsEnabled();
	}
}
