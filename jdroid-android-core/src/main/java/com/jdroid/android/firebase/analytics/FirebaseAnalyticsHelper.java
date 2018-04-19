package com.jdroid.android.firebase.analytics;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.concurrent.LowPriorityThreadFactory;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FirebaseAnalyticsHelper {

	private static final Logger LOGGER = LoggerUtils.getLogger(FirebaseAnalyticsHelper.class);

	private FirebaseAnalytics firebaseAnalytics;

	private Executor executor = Executors.newSingleThreadExecutor(new LowPriorityThreadFactory("firebase-analytics"));

	@SuppressLint("MissingPermission")
	public FirebaseAnalyticsHelper() {
		firebaseAnalytics = FirebaseAnalytics.getInstance(AbstractApplication.get());
	}

	public void sendEvent(String eventName, Map<String, String> params) {
		Bundle bundle = new Bundle();
		if (params != null) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				bundle.putString(entry.getKey(), entry.getValue());
			}
		}
		firebaseAnalytics.logEvent(eventName, bundle);
		LOGGER.debug("Event [" + eventName + "] sent. " + bundle);
	}
	
	@Deprecated
	public void sendEvent(String eventName, Bundle params) {
		firebaseAnalytics.logEvent(eventName, params);
		LOGGER.debug("Event [" + eventName + "] sent. " + params);
	}

	public void sendEvent(String eventName) {
		sendEvent(eventName, (Map<String, String>)null);
	}

	public void setUserProperty(String name, String value) {
		if (value == null) {
			removeUserProperty(name);
		} else {
			firebaseAnalytics.setUserProperty(name, value);
			LOGGER.debug("User Property [" + name + "] added. Value [" + value + "]");
		}
	}

	public void removeUserProperty(String name) {
		firebaseAnalytics.setUserProperty(name, null);
		LOGGER.debug("User Property [" + name + "] removed.");
	}

	public void setUserId(String id) {
		firebaseAnalytics.setUserId(id);
		LOGGER.debug("User Id [" + id + "] added.");
	}

	public void removeUserId() {
		firebaseAnalytics.setUserId(null);
		LOGGER.debug("User Id removed.");
	}

	public Executor getExecutor() {
		return executor;
	}
}
