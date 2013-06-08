package com.jdroid.android.analytics;

import java.util.List;
import android.app.Activity;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.exception.ExceptionHandler;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.exception.ConnectionException;
import com.jdroid.java.utils.ExecutorUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class AnalyticsSender implements AnalyticsTracker {
	
	private List<? extends AnalyticsTracker> trackers;
	
	private static final AnalyticsSender INSTANCE = new AnalyticsSender();
	
	private AnalyticsSender() {
		trackers = Lists.newArrayList(GoogleAnalyticsTracker.get());
	}
	
	public static AnalyticsSender get() {
		return INSTANCE;
	}
	
	private abstract class TrackerRunnable implements Runnable {
		
		@Override
		public void run() {
			try {
				for (AnalyticsTracker tracker : trackers) {
					if (tracker.isEnabled()) {
						track(tracker);
					}
				}
			} catch (Exception e) {
				ExceptionHandler exceptionHandler = AbstractApplication.get().getExceptionHandler();
				if (exceptionHandler != null) {
					exceptionHandler.logHandledException(e);
				}
			}
		}
		
		protected abstract void track(AnalyticsTracker tracker);
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#onActivityStart(android.app.Activity)
	 */
	@Override
	public void onActivityStart(final Activity activity) {
		ExecutorUtils.execute(new TrackerRunnable() {
			
			@Override
			protected void track(AnalyticsTracker tracker) {
				tracker.onActivityStart(activity);
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#onActivityStop(android.app.Activity)
	 */
	@Override
	public void onActivityStop(final Activity activity) {
		ExecutorUtils.execute(new TrackerRunnable() {
			
			@Override
			protected void track(AnalyticsTracker tracker) {
				tracker.onActivityStop(activity);
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#isEnabled()
	 */
	@Override
	public Boolean isEnabled() {
		return null;
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#trackConnectionException(com.jdroid.java.exception.ConnectionException)
	 */
	@Override
	public void trackConnectionException(final ConnectionException connectionException) {
		ExecutorUtils.execute(new TrackerRunnable() {
			
			@Override
			protected void track(AnalyticsTracker tracker) {
				tracker.trackConnectionException(connectionException);
			}
		});
	}
	
}
