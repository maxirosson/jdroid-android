package com.jdroid.android.firebase.analytics;

public class FirebaseAnalyticsFactory {

	private static FirebaseAnalyticsHelper firebaseAnalyticsHelper = new FirebaseAnalyticsHelper();

	public static FirebaseAnalyticsHelper getFirebaseAnalyticsHelper() {
		return firebaseAnalyticsHelper;
	}

	public static void setFirebaseAnalyticsHelper(FirebaseAnalyticsHelper firebaseAnalyticsHelper) {
		FirebaseAnalyticsFactory.firebaseAnalyticsHelper = firebaseAnalyticsHelper;
	}
}
