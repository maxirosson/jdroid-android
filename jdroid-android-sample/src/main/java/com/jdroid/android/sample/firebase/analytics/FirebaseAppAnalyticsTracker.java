package com.jdroid.android.sample.firebase.analytics;

import com.jdroid.android.firebase.analytics.AbstractFirebaseAnalyticsTracker;
import com.jdroid.android.firebase.analytics.FirebaseAnalyticsParams;
import com.jdroid.android.sample.analytics.AppAnalyticsTracker;

public class FirebaseAppAnalyticsTracker extends AbstractFirebaseAnalyticsTracker implements AppAnalyticsTracker {
	
	@Override
	public void trackExampleEvent() {
		FirebaseAnalyticsParams params = new FirebaseAnalyticsParams();
		params.put("example_param", "example_value");
		params.put("example_param_2", true);
		params.put("example_param_3", 1);
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
