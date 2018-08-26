package com.jdroid.android.analytics;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.jdroid.android.social.SocialAction;
import com.jdroid.java.concurrent.LowPriorityThreadFactory;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public abstract class AbstractCoreAnalyticsTracker implements CoreAnalyticsTracker {

	private Executor executor = Executors.newSingleThreadExecutor(new LowPriorityThreadFactory("analytics"));

	@Override
	public Executor getExecutor() {
		return executor;
	}

	@Override
	public void trackErrorLog(@NonNull String message) {
		// Do Nothing
	}

	@Override
	public void trackErrorCustomKey(@NonNull String key, @NonNull Object value) {
		// Do Nothing
	}

	@Override
	public void onFirstActivityCreate(Activity activity) {
		// Do nothing
	}

	@Override
	public void onActivityCreate(Activity activity, Bundle savedInstanceState) {
		// Do nothing
	}

	@Override
	public void onActivityStart(Activity activity, String referrer, Object data) {
		// Do Nothing
	}

	@Override
	public void onActivityResume(Activity activity) {
		// Do Nothing
	}

	@Override
	public void onActivityPause(Activity activity) {
		// Do Nothing
	}

	@Override
	public void onActivityStop(Activity activity) {
		// Do Nothing
	}

	@Override
	public void onActivityDestroy(Activity activity) {
		// Do Nothing
	}

	@Override
	public void onFragmentStart(String screenViewName) {
		// Do Nothing
	}

	@Override
	public void trackHandledException(Throwable throwable, List<String> tags) {
		// Do Nothing
	}

	@Override
	public void trackUriOpened(String screenName, Uri uri, String referrer) {
		// Do Nothing
	}

	@Override
	public void trackSocialInteraction(String network, SocialAction socialAction, String socialTarget) {
		// Do Nothing
	}

	@Override
	public void trackNotificationDisplayed(String notificationName) {
		// Do Nothing
	}

	@Override
	public void trackNotificationOpened(String notificationName) {
		// Do Nothing
	}

	@Override
	public void trackEnjoyingApp(Boolean enjoying) {
		// Do Nothing
	}

	@Override
	public void trackRateOnGooglePlay(Boolean rate) {
		// Do Nothing
	}

	@Override
	public void trackGiveFeedback(Boolean feedback) {
		// Do Nothing
	}

	// Widgets

	public void trackWidgetAdded(String widgetName) {
		// Do Nothing
	}

	public void trackWidgetRemoved(String widgetName) {
		// Do Nothing
	}

	@Override
	public void trackSendAppInvitation(String invitationId) {
		// Do Nothing
	}

	@Override
	public Boolean isEnabled() {
		return true;
	}
}