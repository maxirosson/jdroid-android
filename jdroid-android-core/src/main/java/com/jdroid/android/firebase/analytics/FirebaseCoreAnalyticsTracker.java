package com.jdroid.android.firebase.analytics;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.jdroid.android.analytics.CoreAnalyticsTracker;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.social.SocialAction;
import com.jdroid.android.utils.DeviceUtils;
import com.jdroid.android.utils.ScreenUtils;

import java.util.List;

public class FirebaseCoreAnalyticsTracker extends AbstractFirebaseAnalyticsTracker implements CoreAnalyticsTracker {

	private static final String INSTALLATION_SOURCE_USER_PROPERTY = "INSTALLATION_SOURCE";
	private static final String DEVICE_YEAR_CLASS_USER_PROPERTY = "DEVICE_YEAR_CLASS";
	private static final String SCREEN_WIDTH = "SCREEN_WIDTH";
	private static final String SCREEN_HEIGHT = "SCREEN_HEIGHT";
	private static final String SCREEN_DENSITY = "SCREEN_DENSITY";
	private static final String SCREEN_DENSITY_DPI = "SCREEN_DENSITY_DPI";

	private Boolean firstTrackingSent = false;

	@Override
	public void trackFatalException(Throwable throwable, List<String> tags) {
		// Do nothing
	}

	@Override
	public void trackHandledException(Throwable throwable, List<String> tags) {
		// Do nothing
	}

	@Override
	public void trackErrorLog(String message) {
		// Do nothing
	}
	
	@Override
	public void trackErrorCustomKey(@NonNull String key, @NonNull Object value) {
		// Do nothing
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
		if (!firstTrackingSent) {
			getFirebaseAnalyticsHelper().setUserProperty(DEVICE_YEAR_CLASS_USER_PROPERTY, DeviceUtils.getDeviceYearClass().toString());
			getFirebaseAnalyticsHelper().setUserProperty(SCREEN_WIDTH, ScreenUtils.getScreenWidthDp().toString());
			getFirebaseAnalyticsHelper().setUserProperty(SCREEN_HEIGHT, ScreenUtils.getScreenHeightDp().toString());
			getFirebaseAnalyticsHelper().setUserProperty(SCREEN_DENSITY, ScreenUtils.getScreenDensity());
			getFirebaseAnalyticsHelper().setUserProperty(SCREEN_DENSITY_DPI, ScreenUtils.getDensityDpi().toString());
			getFirebaseAnalyticsHelper().setUserProperty(INSTALLATION_SOURCE_USER_PROPERTY, AbstractApplication.get().getInstallationSource());
			firstTrackingSent = true;
		}
	}

	@Override
	public void onActivityResume(Activity activity) {
		// Do nothing
	}

	@Override
	public void onActivityPause(Activity activity) {
		// Do nothing
	}

	@Override
	public void onActivityStop(Activity activity) {
		// Do nothing
	}

	@Override
	public void onActivityDestroy(Activity activity) {
		// Do nothing
	}

	@Override
	public void onFragmentStart(String screenViewName) {
		// Do nothing
	}

	@Override
	public void trackNotificationDisplayed(String notificationName) {
		Bundle params = new Bundle();
		params.putString("notification_name", notificationName);
		getFirebaseAnalyticsHelper().sendEvent("display_notification", params);
	}

	@Override
	public void trackNotificationOpened(String notificationName) {
		Bundle params = new Bundle();
		params.putString("notification_name", notificationName);
		getFirebaseAnalyticsHelper().sendEvent("open_notification", params);
	}

	@Override
	public void trackEnjoyingApp(Boolean enjoying) {
		Bundle params = new Bundle();
		params.putString("enjoying", enjoying.toString());
		getFirebaseAnalyticsHelper().sendEvent("enjoying_app", params);
	}

	@Override
	public void trackRateOnGooglePlay(Boolean rate) {
		Bundle params = new Bundle();
		params.putString("rate", rate.toString());
		getFirebaseAnalyticsHelper().sendEvent("rate_on_google_play", params);
	}

	@Override
	public void trackGiveFeedback(Boolean feedback) {
		Bundle params = new Bundle();
		params.putString("feedback", feedback.toString());
		getFirebaseAnalyticsHelper().sendEvent("give_feedback", params);
	}

	@Override
	public void trackWidgetAdded(String widgetName) {
		Bundle params = new Bundle();
		params.putString("widget_name", widgetName);
		getFirebaseAnalyticsHelper().sendEvent("add_widget", params);
	}

	@Override
	public void trackWidgetRemoved(String widgetName) {
		Bundle params = new Bundle();
		params.putString("widget_name", widgetName);
		getFirebaseAnalyticsHelper().sendEvent("remove_widget", params);
	}

	@Override
	public void trackUriOpened(String screenName, Uri uri, String referrer) {
		Bundle params = new Bundle();
		params.putString("screen_name", screenName);
		params.putString("referrer", referrer);
		getFirebaseAnalyticsHelper().sendEvent("open_uri", params);
	}

	@Override
	public void trackSocialInteraction(String network, SocialAction socialAction, String socialTarget) {
		Bundle params = new Bundle();
		if (network != null) {
			params.putString("network", network);
		}
		params.putString("social_target", socialTarget);
		getFirebaseAnalyticsHelper().sendEvent(socialAction.getName(), params);
	}

	@Override
	public void trackSendAppInvitation(String invitationId) {
		Bundle params = new Bundle();
		params.putString(FirebaseAnalytics.Param.ITEM_ID, invitationId);
		getFirebaseAnalyticsHelper().sendEvent("send_app_invitation", params);
	}
}
