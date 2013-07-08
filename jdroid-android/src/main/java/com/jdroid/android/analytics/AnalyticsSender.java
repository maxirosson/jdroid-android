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
 * @param <T>
 * @author Maxi Rosson
 */
public class AnalyticsSender<T extends AnalyticsTracker> implements AnalyticsTracker {
	
	private List<T> trackers = Lists.newArrayList();
	
	public AnalyticsSender(T... trackers) {
		this(Lists.newArrayList(trackers));
	}
	
	public AnalyticsSender(List<T> trackers) {
		this.trackers = trackers;
	}
	
	private abstract class TrackerRunnable implements Runnable {
		
		@Override
		public void run() {
			try {
				for (T tracker : trackers) {
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
		
		protected abstract void track(T tracker);
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#isEnabled()
	 */
	@Override
	public Boolean isEnabled() {
		return null;
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#onActivityStart(android.app.Activity)
	 */
	@Override
	public void onActivityStart(final Activity activity) {
		ExecutorUtils.execute(new TrackerRunnable() {
			
			@Override
			protected void track(T tracker) {
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
			protected void track(T tracker) {
				tracker.onActivityStop(activity);
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#trackConnectionException(com.jdroid.java.exception.ConnectionException)
	 */
	@Override
	public void trackConnectionException(final ConnectionException connectionException) {
		ExecutorUtils.execute(new TrackerRunnable() {
			
			@Override
			protected void track(T tracker) {
				tracker.trackConnectionException(connectionException);
			}
		});
	}
	
}
