package com.jdroid.android.sample.firebase.analytics;

import com.jdroid.android.firebase.analytics.AbstractFirebaseAnalyticsTracker;
import com.jdroid.android.firebase.analytics.FirebaseAnalyticsParams;
import com.jdroid.android.sample.analytics.AppAnalyticsTracker;

public class FirebaseAppAnalyticsTracker extends AbstractFirebaseAnalyticsTracker implements AppAnalyticsTracker {
	
	@Override
	public void trackExampleEvent() {
		FirebaseAnalyticsParams params = new FirebaseAnalyticsParams();
		params.put("sample_key_1", "sample_value");
		params.put("sample_key_2", true);
		params.put("sample_key_3", 1);
		params.put("sample_key_with_too_long_name_will_be_truncated", 1);
		params.put("sample_key_with_too_long_value", "sample_value_with_too_long_name_will_be_truncated_and_log_a_warning_123456789012345678901234567890123456789");
		getFirebaseAnalyticsHelper().sendEvent("sample_event_1", params);
		getFirebaseAnalyticsHelper().sendEvent("sample_event_2");
		getFirebaseAnalyticsHelper().sendEvent("sample_event_with_too_long_name");
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
