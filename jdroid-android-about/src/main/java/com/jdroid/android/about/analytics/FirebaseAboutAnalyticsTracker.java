package com.jdroid.android.about.analytics;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.jdroid.android.firebase.analytics.AbstractFirebaseAnalyticsTracker;

public class FirebaseAboutAnalyticsTracker extends AbstractFirebaseAnalyticsTracker implements AboutAnalyticsTracker {

	@Override
	public void trackAboutLibraryOpen(String libraryKey) {
		Bundle params = new Bundle();
		params.putString(FirebaseAnalytics.Param.ITEM_ID, libraryKey);
		getFirebaseAnalyticsHelper().sendEvent("open_library", params);
	}

	@Override
	public void trackContactUs() {
		getFirebaseAnalyticsHelper().sendEvent("contact_us");
	}
}
