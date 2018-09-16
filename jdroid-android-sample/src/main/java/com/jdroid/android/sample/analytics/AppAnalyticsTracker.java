package com.jdroid.android.sample.analytics;

import com.jdroid.java.analytics.AnalyticsTracker;

public interface AppAnalyticsTracker extends AnalyticsTracker {

	public void trackExampleEvent();

	public void trackExampleTransaction();

	public void trackExampleTiming();

}
