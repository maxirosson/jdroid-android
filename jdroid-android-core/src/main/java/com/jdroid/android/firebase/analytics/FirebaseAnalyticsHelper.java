package com.jdroid.android.firebase.analytics;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.context.BuildConfigUtils;
import com.jdroid.android.firebase.testlab.FirebaseTestLab;
import com.jdroid.java.concurrent.LowPriorityThreadFactory;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FirebaseAnalyticsHelper {

	static final Logger LOGGER = LoggerUtils.getLogger(FirebaseAnalyticsHelper.class);

	private Executor executor = Executors.newSingleThreadExecutor(new LowPriorityThreadFactory("firebase-analytics"));

	private static final int EVENT_NAME_MAX_CHARS_LONG = 40;

	public void sendEvent(@NonNull String eventName, @Nullable FirebaseAnalyticsParams params) {

		if (eventName.length() > EVENT_NAME_MAX_CHARS_LONG) {
			LOGGER.warn("Event name [" + eventName + "] must be " + EVENT_NAME_MAX_CHARS_LONG + " chars long as maximum.");
			eventName = eventName.substring(0, EVENT_NAME_MAX_CHARS_LONG - 1);
		}

		if (isFirebaseAnalyticsEnabled()) {
			getFirebaseAnalytics().logEvent(eventName, params != null ? params.getBundle() : null);
			LOGGER.debug("Event [" + eventName + "] sent. " + (params != null ? params : ""));
		} else {
			LOGGER.debug("SKIPPED: Event [" + eventName + "] sent. " + params);
		}
	}

	public void sendEvent(@NonNull String eventName) {
		sendEvent(eventName, (FirebaseAnalyticsParams)null);
	}

	public void setUserProperty(@NonNull String name, @Nullable String value) {
		if (value == null) {
			removeUserProperty(name);
		} else {
			if (isFirebaseAnalyticsEnabled()) {
				getFirebaseAnalytics().setUserProperty(name, value);
				LOGGER.debug("User Property [" + name + "] added. Value [" + value + "]");
			} else {
				LOGGER.debug("SKIPPED: User Property [" + name + "] added. Value [" + value + "]");
			}
		}
	}

	public void removeUserProperty(@NonNull String name) {
		if (isFirebaseAnalyticsEnabled()) {
			getFirebaseAnalytics().setUserProperty(name, null);
			LOGGER.debug("User Property [" + name + "] removed.");
		} else {
			LOGGER.debug("SKIPPED: User Property [" + name + "] removed.");
		}
	}

	public void setUserId(String id) {
		if (isFirebaseAnalyticsEnabled()) {
			getFirebaseAnalytics().setUserId(id);
			LOGGER.debug("User Id [" + id + "] added.");
		} else {
			LOGGER.debug("SKIPPED: User Id [" + id + "] added.");
		}
	}

	public void removeUserId() {
		if (isFirebaseAnalyticsEnabled()) {
			getFirebaseAnalytics().setUserId(null);
			LOGGER.debug("User Id removed.");
		} else {
			LOGGER.debug("SKIPPED: User Id removed.");
		}
	}

	@SuppressLint("MissingPermission")
	private FirebaseAnalytics getFirebaseAnalytics() {
		return FirebaseAnalytics.getInstance(AbstractApplication.get());
	}

	public Executor getExecutor() {
		return executor;
	}

	/**
	 * @return Whether the application has Firebase Analytics enabled or not
	 */
	public Boolean isFirebaseAnalyticsEnabled() {
		return BuildConfigUtils.getBuildConfigBoolean("FIREBASE_ANALYTICS_ENABLED", true) && !FirebaseTestLab.isRunningInstrumentedTests();
	}
}
