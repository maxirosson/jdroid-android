package com.jdroid.android.analytics;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.jdroid.android.social.SocialAction;
import com.jdroid.java.analytics.AnalyticsSender;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.util.List;

/**
 * 
 * @param <T>
 */
public class CoreAnalyticsSender<T extends CoreAnalyticsTracker> extends AnalyticsSender<T> implements CoreAnalyticsTracker {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(CoreAnalyticsSender.class);
	
	@SafeVarargs
	public CoreAnalyticsSender(T... trackers) {
		super(trackers);
	}
	
	public CoreAnalyticsSender(List<T> trackers) {
		super(trackers);
	}
	
	@Override
	public void trackErrorLog(@NonNull String message) {
		execute(new TrackingCommand() {

			@Override
			protected void track(T tracker) {
				tracker.trackErrorLog(message);
			}
		});
	}
	
	@Override
	public void trackErrorCustomKey(@NonNull String key, @NonNull Object value) {
		execute(new TrackingCommand() {
			
			@Override
			protected void track(T tracker) {
				tracker.trackErrorCustomKey(key, value);
			}
		});
	}
	
	@Override
	public void onFirstActivityCreate(final Activity activity) {
		execute(new TrackingCommand() {

			@Override
			protected void track(T tracker) {
				tracker.onFirstActivityCreate(activity);
			}
		});
	}

	@Override
	public void onActivityCreate(final Activity activity, final Bundle savedInstanceState) {
		execute(new TrackingCommand() {

			@Override
			protected void track(T tracker) {
				tracker.onActivityCreate(activity, savedInstanceState);
			}
		});
	}

	@Override
	public void onActivityStart(final Activity activity, final String referrer,
			final Object data) {
		execute(new TrackingCommand() {
			
			@Override
			protected void track(T tracker) {
				tracker.onActivityStart(activity, referrer, data);
			}
		});
	}
	
	@Override
	public void onActivityResume(final Activity activity) {
		execute(new TrackingCommand() {
			@Override
			protected void track(T tracker) {
				tracker.onActivityResume(activity);
			}
		});
	}

	@Override
	public void onActivityPause(final Activity activity) {
		execute(new TrackingCommand() {
			
			@Override
			protected void track(T tracker) {
				tracker.onActivityPause(activity);
			}
		});
	}
	
	@Override
	public void onActivityStop(final Activity activity) {
		execute(new TrackingCommand() {
			
			@Override
			protected void track(T tracker) {
				tracker.onActivityStop(activity);
			}
		});
	}
	
	@Override
	public void onActivityDestroy(final Activity activity) {
		execute(new TrackingCommand() {
			
			@Override
			protected void track(T tracker) {
				tracker.onActivityDestroy(activity);
			}
		});
	}
	
	@Override
	public void onFragmentStart(final String screenViewName) {
		execute(new TrackingCommand() {
			
			@Override
			protected void track(T tracker) {
				tracker.onFragmentStart(screenViewName);
			}
		});
	}

	@Override
	public void trackFatalException(final Throwable throwable, final List<String> tags) {
		execute(new TrackingCommand() {

			@Override
			protected void track(T tracker) {
				tracker.trackFatalException(throwable, tags);
			}
		});
	}
	
	@Override
	public void trackHandledException(final Throwable throwable, final List<String> tags) {
		execute(new TrackingCommand() {

			@Override
			protected void track(T tracker) {
				tracker.trackHandledException(throwable, tags);
			}
		}, false);
	}
	
	@Override
	public void trackUriOpened(final String screenName, final Uri uri, final String referrer) {
		execute(new TrackingCommand() {
			
			@Override
			protected void track(T tracker) {
				tracker.trackUriOpened(screenName, uri, referrer);
			}
		});
	}
	
	@Override
	public void trackSocialInteraction(final String network, final SocialAction socialAction,
									   final String socialTarget) {
		execute(new TrackingCommand() {
			
			@Override
			protected void track(T tracker) {
				tracker.trackSocialInteraction(network, socialAction, socialTarget);
			}
		});
	}
	
	@Override
	public void trackNotificationDisplayed(final String notificationName) {
		execute(new TrackingCommand() {
			
			@Override
			protected void track(T tracker) {
				tracker.trackNotificationDisplayed(notificationName);
			}
		});
	}
	
	@Override
	public void trackNotificationOpened(final String notificationName) {
		execute(new TrackingCommand() {
			
			@Override
			protected void track(T tracker) {
				tracker.trackNotificationOpened(notificationName);
			}
		});
	}

	@Override
	public void trackEnjoyingApp(final Boolean enjoying) {
		execute(new TrackingCommand() {

			@Override
			protected void track(T tracker) {
				tracker.trackEnjoyingApp(enjoying);
			}
		});
	}

	@Override
	public void trackRateOnGooglePlay(final Boolean rate) {
		execute(new TrackingCommand() {

			@Override
			protected void track(T tracker) {
				tracker.trackRateOnGooglePlay(rate);
			}
		});
	}

	@Override
	public void trackGiveFeedback(final Boolean feedback) {
		execute(new TrackingCommand() {

			@Override
			protected void track(T tracker) {
				tracker.trackGiveFeedback(feedback);
			}
		});
	}

	// Widgets

	@Override
	public void trackWidgetAdded(final String widgetName) {
		execute(new TrackingCommand() {

			@Override
			protected void track(T tracker) {
				tracker.trackWidgetAdded(widgetName);
			}
		});
	}

	@Override
	public void trackWidgetRemoved(final String widgetName) {
		execute(new TrackingCommand() {

			@Override
			protected void track(T tracker) {
				tracker.trackWidgetRemoved(widgetName);
			}
		});
	}

	@Override
	public void trackSendAppInvitation(final String invitationId) {
		execute(new TrackingCommand() {

			@Override
			protected void track(T tracker) {
				tracker.trackSendAppInvitation(invitationId);
			}
		});
	}
}
