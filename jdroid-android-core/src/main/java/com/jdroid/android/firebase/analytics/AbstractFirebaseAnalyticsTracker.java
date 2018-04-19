package com.jdroid.android.firebase.analytics;

import android.support.annotation.WorkerThread;

import com.jdroid.java.analytics.AnalyticsTracker;

import java.util.concurrent.Executor;

public class AbstractFirebaseAnalyticsTracker implements AnalyticsTracker {

	@Override
	public Boolean isEnabled() {
		return getFirebaseAnalyticsHelper().isFirebaseAnalyticsEnabled();
	}

	@WorkerThread
	protected FirebaseAnalyticsHelper getFirebaseAnalyticsHelper() {
		return FirebaseAnalyticsFactory.getFirebaseAnalyticsHelper();
	}

	@Override
	public Executor getExecutor() {
		return getFirebaseAnalyticsHelper().getExecutor();
	}
}
