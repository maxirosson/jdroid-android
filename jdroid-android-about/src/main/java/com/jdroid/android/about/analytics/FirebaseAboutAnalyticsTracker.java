package com.jdroid.android.about.analytics;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.jdroid.android.firebase.analytics.AbstractFirebaseAnalyticsTracker;
import com.jdroid.java.collections.Maps;

import java.util.Map;

public class FirebaseAboutAnalyticsTracker extends AbstractFirebaseAnalyticsTracker implements AboutAnalyticsTracker {

	@Override
	public void trackAboutLibraryOpen(String libraryKey) {
		Map<String, String> params = Maps.newHashMap();
		params.put(FirebaseAnalytics.Param.ITEM_ID, libraryKey);
		getFirebaseAnalyticsHelper().sendEvent("open_library", params);
	}

	@Override
	public void trackContactUs() {
		getFirebaseAnalyticsHelper().sendEvent("contact_us");
	}
}
