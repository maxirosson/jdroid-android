package com.jdroid.android.google.analytics;

import androidx.annotation.WorkerThread;

import com.jdroid.java.analytics.AnalyticsTracker;

import java.util.concurrent.Executor;

public class AbstractGoogleAnalyticsTracker implements AnalyticsTracker {

	@Override
	public Boolean isEnabled() {
		return GoogleAnalyticsAppContext.isGoogleAnalyticsEnabled();
	}

	@WorkerThread
	protected GoogleAnalyticsHelper getGoogleAnalyticsHelper() {
		return GoogleAnalyticsFactory.getGoogleAnalyticsHelper();
	}

	@Override
	public Executor getExecutor() {
		return getGoogleAnalyticsHelper().getExecutor();
	}
}
