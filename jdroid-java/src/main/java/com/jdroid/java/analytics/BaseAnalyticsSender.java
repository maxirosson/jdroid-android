package com.jdroid.java.analytics;

import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.util.List;

public class BaseAnalyticsSender<T extends BaseAnalyticsTracker> implements BaseAnalyticsTracker {

	private static final Logger LOGGER = LoggerUtils.getLogger(BaseAnalyticsSender.class);

	private List<T> trackers = Lists.newArrayList();

	@SafeVarargs
	public BaseAnalyticsSender(T... trackers) {
		this(Lists.newArrayList(trackers));
	}

	public BaseAnalyticsSender(List<T> trackers) {
		for (T tracker : trackers) {
			if (tracker.isEnabled()) {
				this.trackers.add(tracker);
			}
		}
	}

	@Override
	public Boolean isEnabled() {
		return false;
	}

	protected List<T> getTrackers() {
		return trackers;
	}

	public void addTracker(T tracker) {
		trackers.add(tracker);
	}

	public abstract class TrackerRunnable implements Runnable {

		@Override
		public void run() {
			for (T tracker : getTrackers()) {
				try {
					if (tracker.isEnabled()) {
						track(tracker);
					}
				} catch (Exception e) {
					LoggerUtils.logHandledException(LOGGER, e);
				}
			}
		}

		protected abstract void track(T tracker);
	}

}
