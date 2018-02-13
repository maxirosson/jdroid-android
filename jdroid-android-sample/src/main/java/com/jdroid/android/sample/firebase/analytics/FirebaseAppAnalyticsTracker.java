package com.jdroid.android.sample.firebase.analytics;

import android.os.Bundle;

import com.jdroid.android.firebase.analytics.AbstractFirebaseAnalyticsTracker;
import com.jdroid.android.sample.analytics.AppAnalyticsTracker;

public class FirebaseAppAnalyticsTracker extends AbstractFirebaseAnalyticsTracker implements AppAnalyticsTracker {
	
	@Override
	public void trackExampleEvent() {
		Bundle bundle = new Bundle();
		bundle.putString("example_param", "example_value");
		getFirebaseAnalyticsHelper().sendEvent("example_action", bundle);
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
