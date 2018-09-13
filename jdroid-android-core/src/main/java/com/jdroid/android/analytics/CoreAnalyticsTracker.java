package com.jdroid.android.analytics;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.jdroid.android.social.SocialAction;
import com.jdroid.java.analytics.AnalyticsTracker;

import java.util.List;

public interface CoreAnalyticsTracker extends AnalyticsTracker {

	// Error handling

	public void trackHandledException(Throwable throwable, List<String> tags);

	public void trackErrorLog(@NonNull String message);

	public void trackErrorCustomKey(@NonNull String key, @NonNull Object value);

	// Activity/fragment life cycle

	public void onFirstActivityCreate(Activity activity);

	public void onActivityCreate(Activity activity, Bundle savedInstanceState);

	public void onActivityStart(Activity activity, String referrer, Object data);

	public void onActivityResume(Activity activity);

	public void onActivityPause(Activity activity);

	public void onActivityStop(Activity activity);

	public void onActivityDestroy(Activity activity);

	public void onFragmentStart(String screenViewName);

	// Notifications

	public void trackNotificationDisplayed(String notificationName);

	public void trackNotificationOpened(String notificationName);

	// Feedback

	public void trackEnjoyingApp(Boolean enjoying);

	public void trackRateOnGooglePlay(Boolean rate);

	public void trackGiveFeedback(Boolean feedback);

	// Widgets

	public void trackWidgetAdded(String widgetName);

	public void trackWidgetRemoved(String widgetName);

	// App Invitations

	public void trackSendAppInvitation(String invitationId);

	// More

	public void trackUriOpened(String screenName, Uri uri, String referrer);

	public void trackSocialInteraction(String network, SocialAction socialAction, String socialTarget);

}
