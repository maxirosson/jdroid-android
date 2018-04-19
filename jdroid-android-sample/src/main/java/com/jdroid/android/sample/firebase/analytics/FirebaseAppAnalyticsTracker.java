package com.jdroid.android.sample.firebase.analytics;

import com.jdroid.android.firebase.analytics.AbstractFirebaseAnalyticsTracker;
import com.jdroid.android.sample.analytics.AppAnalyticsTracker;
import com.jdroid.java.collections.Maps;

import java.util.Map;

public class FirebaseAppAnalyticsTracker extends AbstractFirebaseAnalyticsTracker implements AppAnalyticsTracker {
	
	@Override
	public void trackExampleEvent() {
		Map<String, String> params = Maps.newHashMap();
		params.put("example_param", "example_value");
		getFirebaseAnalyticsHelper().sendEvent("example_action", params);
		getFirebaseAnalyticsHelper().sendEvent("example_action_without_params");
	}

	@Override
	public void trackExampleTransaction() {
		// Do nothing
	}

	@Override
	public void trackExampleTiming() {
		// Do nothing
	}
	
}
