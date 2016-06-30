package com.jdroid.android.analytics;

import android.app.Activity;

import com.jdroid.android.social.AccountType;
import com.jdroid.android.social.SocialAction;
import com.jdroid.android.usecase.AbstractUseCase;
import com.jdroid.java.analytics.BaseAnalyticsSender;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * 
 * @param <T>
 */
public class AnalyticsSender<T extends AnalyticsTracker> extends BaseAnalyticsSender<T> implements AnalyticsTracker {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(AnalyticsSender.class);
	
	@SafeVarargs
	public AnalyticsSender(T... trackers) {
		super(trackers);
	}
	
	public AnalyticsSender(List<T> trackers) {
		super(trackers);
	}
	
	@Override
	public void onInitExceptionHandler(Map<String, String> metadata) {
		try {
			for (T tracker : getTrackers()) {
				if (tracker.isEnabled()) {
					tracker.onInitExceptionHandler(metadata);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error when initializing the exception handler", e);
		}
	}

	@Override
	public void trackErrorBreadcrumb(final String message) {
		ExecutorUtils.execute(new TrackerRunnable() {

			@Override
			protected void track(T tracker) {
				tracker.trackErrorBreadcrumb(message);
			}
		});
	}

	@Override
	public void onActivityStart(final Class<? extends Activity> activityClass, final AppLoadingSource appLoadingSource,
			final Object data) {
		ExecutorUtils.execute(new TrackerRunnable() {
			
			@Override
			protected void track(T tracker) {
				tracker.onActivityStart(activityClass, appLoadingSource, data);
			}
		});
	}
	
	@Override
	public void onActivityResume(final Activity activity) {
		ExecutorUtils.execute(new TrackerRunnable() {
			
			@Override
			protected void track(T tracker) {
				tracker.onActivityResume(activity);
			}
		});
	}
	
	@Override
	public void onActivityPause(final Activity activity) {
		ExecutorUtils.execute(new TrackerRunnable() {
			
			@Override
			protected void track(T tracker) {
				tracker.onActivityPause(activity);
			}
		});
	}
	
	@Override
	public void onActivityStop(final Activity activity) {
		ExecutorUtils.execute(new TrackerRunnable() {
			
			@Override
			protected void track(T tracker) {
				tracker.onActivityStop(activity);
			}
		});
	}
	
	@Override
	public void onActivityDestroy(final Activity activity) {
		ExecutorUtils.execute(new TrackerRunnable() {
			
			@Override
			protected void track(T tracker) {
				tracker.onActivityDestroy(activity);
			}
		});
	}
	
	@Override
	public void onFragmentStart(final String screenViewName) {
		ExecutorUtils.execute(new TrackerRunnable() {
			
			@Override
			protected void track(T tracker) {
				tracker.onFragmentStart(screenViewName);
			}
		});
	}

	@Override
	public void trackFatalException(final Throwable throwable, final List<String> tags) {
		ExecutorUtils.execute(() -> {
            for (T tracker : getTrackers()) {
                if (tracker.isEnabled()) {
                    tracker.trackFatalException(throwable, tags);
                }
            }
        });
	}
	
	@Override
	public void trackHandledException(final Throwable throwable, final List<String> tags) {
		ExecutorUtils.execute(() -> {
            for (T tracker : getTrackers()) {
                try {
                    if (tracker.isEnabled()) {
                        tracker.trackHandledException(throwable, tags);
                    }
                } catch (Exception e) {
                    LOGGER.error("Error when trying to track the exception.", e);
                }
            }
        });
	}
	
	@Override
	public void trackUriOpened(final String uriType, final String screenName) {
		ExecutorUtils.execute(new TrackerRunnable() {
			
			@Override
			protected void track(T tracker) {
				tracker.trackUriOpened(uriType, screenName);
			}
		});
	}
	
	@Override
	public void trackSocialInteraction(final AccountType accountType, final SocialAction socialAction,
			final String socialTarget) {
		ExecutorUtils.execute(new TrackerRunnable() {
			
			@Override
			protected void track(T tracker) {
				tracker.trackSocialInteraction(accountType, socialAction, socialTarget);
			}
		});
	}
	
	@Override
	public void trackNotificationDisplayed(final String notificationName) {
		ExecutorUtils.execute(new TrackerRunnable() {
			
			@Override
			protected void track(T tracker) {
				tracker.trackNotificationDisplayed(notificationName);
			}
		});
	}
	
	@Override
	public void trackNotificationOpened(final String notificationName) {
		ExecutorUtils.execute(new TrackerRunnable() {
			
			@Override
			protected void track(T tracker) {
				tracker.trackNotificationOpened(notificationName);
			}
		});
	}

	@Override
	public void trackEnjoyingApp(final Boolean enjoying) {
		ExecutorUtils.execute(new TrackerRunnable() {

			@Override
			protected void track(T tracker) {
				tracker.trackEnjoyingApp(enjoying);
			}
		});
	}

	@Override
	public void trackRateOnGooglePlay(final Boolean rate) {
		ExecutorUtils.execute(new TrackerRunnable() {

			@Override
			protected void track(T tracker) {
				tracker.trackRateOnGooglePlay(rate);
			}
		});
	}

	@Override
	public void trackGiveFeedback(final Boolean feedback) {
		ExecutorUtils.execute(new TrackerRunnable() {

			@Override
			protected void track(T tracker) {
				tracker.trackGiveFeedback(feedback);
			}
		});
	}

	@Override
	public void trackUseCaseTiming(final Class<? extends AbstractUseCase> useCaseClass, final long executionTime) {
		ExecutorUtils.execute(new TrackerRunnable() {

			@Override
			protected void track(T tracker) {
				tracker.trackUseCaseTiming(useCaseClass, executionTime);
			}
		});
	}

	@Override
	public void trackServiceTiming(final String trackingVariable, final String trackingLabel, final long executionTime) {
		ExecutorUtils.execute(new TrackerRunnable() {

			@Override
			protected void track(T tracker) {
				tracker.trackServiceTiming(trackingVariable, trackingLabel, executionTime);
			}
		});
	}
}
