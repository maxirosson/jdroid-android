package com.jdroid.android.about.analytics;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.jdroid.android.firebase.analytics.AbstractFirebaseAnalyticsTracker;
import com.jdroid.android.firebase.analytics.FirebaseAnalyticsParams;

public class FirebaseAboutAnalyticsTracker extends AbstractFirebaseAnalyticsTracker implements AboutAnalyticsTracker {

	@Override
	public void trackAboutLibraryOpen(String libraryKey) {
		FirebaseAnalyticsParams params = new FirebaseAnalyticsParams();
		params.put(FirebaseAnalytics.Param.ITEM_ID, libraryKey);
		getFirebaseAnalyticsHelper().sendEvent("open_library", params);
	}

	@Override
	public void trackContactUs() {
		getFirebaseAnalyticsHelper().sendEvent("contact_us");
	}
}
