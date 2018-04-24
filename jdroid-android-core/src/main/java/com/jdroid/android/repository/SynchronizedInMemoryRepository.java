package com.jdroid.android.repository;

import com.jdroid.java.domain.Identifiable;
import com.jdroid.java.repository.InMemoryRepository;
import com.jdroid.java.date.DateUtils;

import java.util.concurrent.TimeUnit;

/**
 * 
 * @param <T>
 */
public class SynchronizedInMemoryRepository<T extends Identifiable> extends InMemoryRepository<T> implements
		SynchronizedRepository<T> {
	
	// Default Refresh frequency (in milliseconds)
	private static final Long DEFAULT_REFRESH_FREQUENCY = TimeUnit.MINUTES.toMillis(5);
	
	private Long lastUpdateTimestamp;
	
	@Override
	public void refreshUpdateTimestamp() {
		lastUpdateTimestamp = DateUtils.nowMillis();
	}
	
	@Override
	public Boolean isOutdated() {
		return (lastUpdateTimestamp == null)
				|| ((lastUpdateTimestamp + getRefreshFrequency()) < DateUtils.nowMillis());
	}
	
	@Override
	public Long getLastUpdateTimestamp() {
		return lastUpdateTimestamp;
	}
	
	@Override
	public void resetLastUpdateTimestamp() {
		lastUpdateTimestamp = null;
	}
	
	protected Long getRefreshFrequency() {
		return DEFAULT_REFRESH_FREQUENCY;
	}
}
