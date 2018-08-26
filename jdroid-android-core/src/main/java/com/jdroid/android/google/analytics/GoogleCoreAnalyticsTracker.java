package com.jdroid.android.google.analytics;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.HitBuilders.SocialBuilder;
import com.jdroid.android.analytics.CoreAnalyticsTracker;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.social.SocialAction;
import com.jdroid.android.social.SocialNetwork;
import com.jdroid.android.utils.DeviceUtils;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.util.List;

public class GoogleCoreAnalyticsTracker extends AbstractGoogleAnalyticsTracker implements CoreAnalyticsTracker {

	private static final Logger LOGGER = LoggerUtils.getLogger(GoogleCoreAnalyticsTracker.class);

	public static final String NOTIFICATION_CATEGORY = "notification";
	private static final String FEEDBACK_CATEGORY = "feedback";
	private static final String WIDGET_CATEGORY = "widget";

	public static final String SOCIAL = "social";

	private Boolean firstTrackingSent = false;

	public enum CustomDimension {
		LOGIN_SOURCE,
		IS_LOGGED,
		INSTALLATION_SOURCE, // User scope
		REFERRER, // Hit scope
		DEVICE_TYPE, // User scope
		DEVICE_YEAR_CLASS; // User scope
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

		synchronized (GoogleCoreAnalyticsTracker.class) {

			HitBuilders.ScreenViewBuilder screenViewBuilder = new HitBuilders.ScreenViewBuilder();
			Uri uri = activity.getIntent().getData();
			if (uri != null) {
				screenViewBuilder.setCampaignParamsFromUrl(uri.toString());
			}
			initReferrerCustomDimension(referrer);


			if (!firstTrackingSent) {
				getGoogleAnalyticsHelper().addCustomDimension(screenViewBuilder, CustomDimension.DEVICE_YEAR_CLASS, DeviceUtils.getDeviceYearClass().toString());
				getGoogleAnalyticsHelper().addCustomDimension(screenViewBuilder, CustomDimension.DEVICE_TYPE, DeviceUtils.getDeviceType());

				String installationSource = AbstractApplication.get().getInstallationSource();
				if (installationSource != null) {
					getGoogleAnalyticsHelper().addCustomDimension(screenViewBuilder, CustomDimension.INSTALLATION_SOURCE, installationSource);

					onAppLoadTrack(screenViewBuilder, data);
					firstTrackingSent = true;
				}
			}
			onActivityStartTrack(screenViewBuilder, data);
			getGoogleAnalyticsHelper().sendScreenView(screenViewBuilder, activity.getClass().getSimpleName());
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

	protected void onAppLoadTrack(HitBuilders.ScreenViewBuilder screenViewBuilder, Object data) {
		// Do nothing
	}

	protected void onActivityStartTrack(HitBuilders.ScreenViewBuilder screenViewBuilder, Object data) {
		// Do nothing
	}

	private void initReferrerCustomDimension(String referrer) {
		if (referrer != null) {
			getGoogleAnalyticsHelper().addCommonCustomDimension(CustomDimension.REFERRER.name(), referrer);
		} else if (!getGoogleAnalyticsHelper().hasCommonCustomDimension(CustomDimension.REFERRER.name())) {
			getGoogleAnalyticsHelper().addCommonCustomDimension(CustomDimension.REFERRER.name(), "normal");
		}
	}

	@Override
	public void onFragmentStart(String screenViewName) {
		synchronized (GoogleCoreAnalyticsTracker.class) {
			HitBuilders.ScreenViewBuilder screenViewBuilder = new HitBuilders.ScreenViewBuilder();
			getGoogleAnalyticsHelper().sendScreenView(screenViewBuilder, screenViewName);
		}
	}

	@Override
	public void trackNotificationDisplayed(String notificationName) {
		getGoogleAnalyticsHelper().sendEvent(NOTIFICATION_CATEGORY, "display", notificationName);
	}

	@Override
	public void trackNotificationOpened(String notificationName) {
		getGoogleAnalyticsHelper().sendEvent(NOTIFICATION_CATEGORY, "open", notificationName);
	}

	@Override
	public void trackEnjoyingApp(Boolean enjoying) {
		getGoogleAnalyticsHelper().sendEvent(FEEDBACK_CATEGORY, "enjoying", enjoying.toString());
	}

	@Override
	public void trackRateOnGooglePlay(Boolean rate) {
		getGoogleAnalyticsHelper().sendEvent(FEEDBACK_CATEGORY, "rate", rate.toString());
	}

	@Override
	public void trackGiveFeedback(Boolean feedback) {
		getGoogleAnalyticsHelper().sendEvent(FEEDBACK_CATEGORY, "giveFeedback", feedback.toString());
	}

	@Override
	public void trackUriOpened(String screenName, Uri uri, String referrer) {
		initReferrerCustomDimension(referrer);
		getGoogleAnalyticsHelper().sendEvent("uri", "open", screenName);
	}

	// Widgets

	public void trackWidgetAdded(String widgetName) {
		getGoogleAnalyticsHelper().sendEvent(WIDGET_CATEGORY, "add", widgetName);
	}

	public void trackWidgetRemoved(String widgetName) {
		getGoogleAnalyticsHelper().sendEvent(WIDGET_CATEGORY, "remove", widgetName);
	}

	@Override
	public void trackSendAppInvitation(String invitationId) {
		getGoogleAnalyticsHelper().sendEvent(GoogleCoreAnalyticsTracker.SOCIAL, "sendAppInvitation", invitationId);
	}

	@Override
	public void trackSocialInteraction(String network, SocialAction socialAction, String socialTarget) {

		String category = SOCIAL;
		if (network != null) {
			category += "-" + network;
		} else {
			network = "Undefined";
		}
		getGoogleAnalyticsHelper().sendEvent(category, socialAction.getName(), socialTarget);

		SocialBuilder socialBuilder = new SocialBuilder();
		socialBuilder.setNetwork(network);
		socialBuilder.setAction(socialAction.getName());
		socialBuilder.setTarget(socialTarget);

		getGoogleAnalyticsHelper().getTracker().send(socialBuilder.build());
		LOGGER.debug("Social interaction sent. Network [" + network + "] Action [" + socialAction.getName()
			+ "] Target [" + socialTarget + "]");
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

	public void sendSocialInteraction(SocialNetwork socialNetwork, SocialAction socialAction, String socialTarget) {

		SocialBuilder socialBuilder = new SocialBuilder();
		socialBuilder.setNetwork(socialNetwork.getName());
		socialBuilder.setAction(socialAction.getName());
		socialBuilder.setTarget(socialTarget);

		getGoogleAnalyticsHelper().getTracker().send(socialBuilder.build());
		LOGGER.debug("Social interaction sent. Network [" + socialNetwork.getName() + "] Action ["
			+ socialAction.getName() + "] Target [" + socialTarget + "]");
	}
}
