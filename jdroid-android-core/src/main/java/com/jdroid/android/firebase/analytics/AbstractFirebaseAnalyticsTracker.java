package com.jdroid.android.firebase.analytics;

import com.jdroid.java.analytics.AnalyticsTracker;

import java.util.concurrent.Executor;

import androidx.annotation.WorkerThread;

public class AbstractFirebaseAnalyticsTracker implements AnalyticsTracker {

	@Override
	public Boolean isEnabled() {
		return true;
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
