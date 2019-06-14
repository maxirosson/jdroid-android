package com.jdroid.android.analytics;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.play.core.splitinstall.SplitInstallSessionState;
import com.jdroid.android.social.SocialAction;
import com.jdroid.java.analytics.AnalyticsTracker;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface CoreAnalyticsTracker extends AnalyticsTracker {

	// Error handling

	void trackHandledException(Throwable throwable, List<String> tags);

	void trackErrorLog(@NonNull String message);

	void trackErrorCustomKey(@NonNull String key, @NonNull Object value);

	// Activity/fragment life cycle

	void onFirstActivityCreate(Activity activity);

	void onActivityCreate(Activity activity, @Nullable Bundle savedInstanceState);

	void onActivityStart(Activity activity, @Nullable String referrer, @Nullable Object data);

	void onActivityResume(Activity activity);

	void onActivityPause(Activity activity);

	void onActivityStop(Activity activity);

	void onActivityDestroy(Activity activity);

	void onFragmentStart(String screenViewName);

	// Notifications

	void trackNotificationDisplayed(String notificationName);

	void trackNotificationOpened(String notificationName);

	// Feedback

	void trackEnjoyingApp(Boolean enjoying);

	void trackRateOnGooglePlay(Boolean rate);

	void trackGiveFeedback(Boolean feedback);

	// Widgets

	void trackWidgetAdded(String widgetName);

	void trackWidgetRemoved(String widgetName);
	
	// Split Install

	void trackSplitInstallStatus(String moduleName, SplitInstallSessionState state);

	void trackSplitInstallUninstalled(String moduleName);

	// More

	void trackUriOpened(String screenName, Uri uri, String referrer);

	void trackSocialInteraction(String network, SocialAction socialAction, String socialTarget);

}
