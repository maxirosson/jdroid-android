package com.jdroid.android.firebase.crashlytics;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.crashlytics.android.Crashlytics;
import com.jdroid.android.analytics.AbstractCoreAnalyticsTracker;
import com.jdroid.android.exception.DefaultExceptionHandler;
import com.jdroid.android.uri.ReferrerUtils;

import java.util.List;


public class FirebaseCrashlyticsTracker extends AbstractCoreAnalyticsTracker {
	
	@Override
	public void trackHandledException(Throwable throwable, List<String> tags) {
		if (areTagsEnabled()) {
			DefaultExceptionHandler.addTags(throwable, tags);
		}
		Crashlytics.logException(throwable);
	}

	protected Boolean areTagsEnabled() {
		return false;
	}

	@Override
	public void trackErrorLog(@NonNull String message) {
		Crashlytics.log(message);
	}
	
	@Override
	public void trackErrorCustomKey(@NonNull String key, @NonNull Object value) {
		if (value instanceof Boolean) {
			Crashlytics.setBool(key, (Boolean)value);
		} else if (value instanceof Double) {
			Crashlytics.setDouble(key, (Double)value);
		} else if (value instanceof Float) {
			Crashlytics.setFloat(key, (Float)value);
		} else if (value instanceof Integer) {
			Crashlytics.setInt(key, (Integer)value);
		} else {
			Crashlytics.setString(key, value.toString());
		}
	}
	
	@Override
	public void onActivityCreate(Activity activity, Bundle savedInstanceState) {
		Crashlytics.log(activity.getClass().getSimpleName() + " created. SavedInstanceState " + (savedInstanceState != null ? "not null" : "null"));
	}

	@Override
	public void onActivityStart(Activity activity, String referrer, Object data) {
		StringBuilder messageBuilder = new StringBuilder();
		messageBuilder.append(activity.getClass().getSimpleName());
		messageBuilder.append(" started");
		if (!ReferrerUtils.isUndefined(referrer)) {
			messageBuilder.append(". Referrer: ");
			messageBuilder.append(referrer);
		}
		Crashlytics.log(messageBuilder.toString());
	}

	@Override
	public void onActivityResume(Activity activity) {
		Crashlytics.log(activity.getClass().getSimpleName() + " resumed");
	}

	@Override
	public void onActivityPause(Activity activity) {
		Crashlytics.log(activity.getClass().getSimpleName() + " paused");
	}

	@Override
	public void onActivityStop(Activity activity) {
		Crashlytics.log(activity.getClass().getSimpleName() + " stopped");
	}

	@Override
	public void onActivityDestroy(Activity activity) {
		Crashlytics.log(activity.getClass().getSimpleName() + " destroyed");
	}
	
	@Override
	public void onFragmentStart(String screenViewName) {
		Crashlytics.log(screenViewName + " started");
	}
	
	@Override
	public Boolean isEnabled() {
		return FirebaseCrashlyticsContext.isFirebaseCrashlyticsEnabled();
	}
}