package com.jdroid.android.repository;

import com.jdroid.java.domain.Identifiable;
import com.jdroid.java.repository.InMemoryRepository;
import com.jdroid.java.date.DateUtils;

/**
 * 
 * @param <T>
 */
public class SynchronizedInMemoryRepository<T extends Identifiable> extends InMemoryRepository<T> implements
		SynchronizedRepository<T> {
	
	// Default Refresh frequency (in milliseconds)
	private static final Long DEFAULT_REFRESH_FREQUENCY = 5 * DateUtils.MILLIS_PER_MINUTE; // 5 min
	
	private Long lastUpdateTimestamp;
	
	/**
	 * @see com.jdroid.android.repository.SynchronizedRepository#refreshUpdateTimestamp()
	 */
	@Override
	public void refreshUpdateTimestamp() {
		lastUpdateTimestamp = DateUtils.nowMillis();
	}
	
	/**
	 * @see com.jdroid.android.repository.SynchronizedRepository#isOutdated()
	 */
	@Override
	public Boolean isOutdated() {
		return (lastUpdateTimestamp == null)
				|| ((lastUpdateTimestamp + getRefreshFrequency()) < DateUtils.nowMillis());
	}
	
	/**
	 * @see com.jdroid.android.repository.SynchronizedRepository#getLastUpdateTimestamp()
	 */
	@Override
	public Long getLastUpdateTimestamp() {
		return lastUpdateTimestamp;
	}
	
	/**
	 * @see com.jdroid.android.repository.SynchronizedRepository#resetLastUpdateTimestamp()
	 */
	@Override
	public void resetLastUpdateTimestamp() {
		lastUpdateTimestamp = null;
	}
	
	protected Long getRefreshFrequency() {
		return DEFAULT_REFRESH_FREQUENCY;
	}
}
