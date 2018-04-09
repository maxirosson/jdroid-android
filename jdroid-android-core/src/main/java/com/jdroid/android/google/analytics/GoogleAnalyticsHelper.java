package com.jdroid.android.google.analytics;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.context.UsageStats;
import com.jdroid.java.collections.Maps;
import com.jdroid.java.concurrent.LowPriorityThreadFactory;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GoogleAnalyticsHelper {

	private static final Logger LOGGER = LoggerUtils.getLogger(GoogleAnalyticsHelper.class);

	// 30 minutes
	private static final int SESSION_TIMEOUT = 1800;

	private Tracker tracker;

	private Map<String, Integer> customDimensionsMap = Maps.newHashMap();
	private Map<String, Integer> customMetricsMap = Maps.newHashMap();

	private Map<String, String> commonCustomDimensionsValues = Maps.newHashMap();

	private Executor executor = Executors.newSingleThreadExecutor(new LowPriorityThreadFactory("google-analytics"));
	
	public synchronized Tracker getTracker() {
		if (tracker == null) {
			GoogleAnalytics analytics = GoogleAnalytics.getInstance(AbstractApplication.get());
			tracker = analytics.newTracker(GoogleAnalyticsAppContext.getGoogleAnalyticsTrackingId());
			tracker.setSessionTimeout(SESSION_TIMEOUT);
			tracker.enableAdvertisingIdCollection(isAdvertisingIdCollectionEnabled());
		}
		return tracker;
	}

	public void addCustomDimensionDefinition(String dimensionName, Integer index) {
		customDimensionsMap.put(dimensionName, index);
	}

	public void addCustomMetricDefinition(String metricName, Integer index) {
		customMetricsMap.put(metricName, index);
	}

	protected Boolean isAdvertisingIdCollectionEnabled() {
		return true;
	}

	public void sendEvent(String category, String action, String label) {
		sendEvent(category, action, label, null, null);
	}

	public void sendEvent(String category, String action, String label, Map<String, String> customDimensions) {
		sendEvent(category, action, label, null, customDimensions);
	}

	public void sendEvent(String category, String action, String label, Long value, Map<String, String> customDimensions) {
		HitBuilders.EventBuilder eventBuilder = new HitBuilders.EventBuilder();

		addCustomDimension(eventBuilder, commonCustomDimensionsValues);
		addCustomDimension(eventBuilder, customDimensions);

		eventBuilder.setCategory(category);
		eventBuilder.setAction(action);
		eventBuilder.setLabel(label);
		if (value != null) {
			eventBuilder.setValue(value);
		}

		getTracker().send(eventBuilder.build());
		LOGGER.debug("Event sent. Category [" + category + "] Action [" + action + "] Label [" + label + "]"
				+ (value != null ? " Value" + value + "]" : ""));
	}

	public void sendScreenView(String screenName) {
		sendScreenView(new HitBuilders.ScreenViewBuilder(), screenName);
	}

	public void sendScreenView(HitBuilders.ScreenViewBuilder screenViewBuilder, String screenName) {

		addCustomDimension(screenViewBuilder, commonCustomDimensionsValues);

		getTracker().setScreenName(screenName);
		getTracker().send(screenViewBuilder.build());
		LOGGER.debug("Screen view sent. Screen name [" + screenName + "]");
	}

	public void sendTransaction(HitBuilders.TransactionBuilder transactionBuilder) {
		sendTransaction(transactionBuilder, null);
	}

	public void sendTransaction(HitBuilders.TransactionBuilder transactionBuilder, Map<String, String> customDimensions) {

		addCustomDimension(transactionBuilder, commonCustomDimensionsValues);
		addCustomDimension(transactionBuilder, customDimensions);

		getTracker().send(transactionBuilder.build());
		LOGGER.debug("Transaction sent. " + transactionBuilder.build());
	}

	public void sendTransactionItem(HitBuilders.ItemBuilder itemBuilder) {
		getTracker().send(itemBuilder.build());
		LOGGER.debug("Transaction item sent. " + itemBuilder.build());
	}

	public void trackTiming(String category, String variable, String label, long value) {
		HitBuilders.TimingBuilder timingBuilder = new HitBuilders.TimingBuilder();
		timingBuilder.setCategory(category);
		timingBuilder.setVariable(variable);
		timingBuilder.setLabel(label);
		timingBuilder.setValue(value);
		timingBuilder.setNonInteraction(true);

		addCustomDimension(timingBuilder, commonCustomDimensionsValues);

		getTracker().send(timingBuilder.build());
		LOGGER.debug("Timing sent. Category [" + category + "] Variable [" + variable + "] Label [" + label
				+ "] Value [" + value + "]");
	}

	protected void addCustomDimension(HitBuilders.ScreenViewBuilder screenViewBuilder, GoogleCoreAnalyticsTracker.CustomDimension customDimension, String dimension) {
		addCustomDimension(screenViewBuilder, customDimension.name(), dimension);
	}

	protected void addCustomDimension(HitBuilders.ScreenViewBuilder screenViewBuilder, String customDimension, String dimension) {
		Integer index = customDimensionsMap.get(customDimension);
		addCustomDimension(screenViewBuilder, index, dimension);
	}

	protected void addCustomDimension(HitBuilders.ScreenViewBuilder screenViewBuilder, Integer index, String dimension) {
		if (index != null) {
			LOGGER.debug("Added custom dimension: " + index + " - " + dimension);
			screenViewBuilder.setCustomDimension(index, dimension);
		}
	}

	protected void addCustomDimension(HitBuilders.ScreenViewBuilder screenViewBuilder, Map<String, String> customDimensions) {
		if (customDimensions != null) {
			for (Map.Entry<String, String> entry : customDimensions.entrySet()) {
				addCustomDimension(screenViewBuilder, entry.getKey(), entry.getValue());
			}
		}
	}

	protected void addCustomDimension(HitBuilders.EventBuilder eventBuilder, Map<String, String> customDimensions) {
		if (customDimensions != null) {
			for (Map.Entry<String, String> entry : customDimensions.entrySet()) {
				addCustomDimension(eventBuilder, entry.getKey(), entry.getValue());
			}
		}
	}
	protected void addCustomDimension(HitBuilders.TimingBuilder timingBuilder, Map<String, String> customDimensions) {
		if (customDimensions != null) {
			for (Map.Entry<String, String> entry : customDimensions.entrySet()) {
				addCustomDimension(timingBuilder, entry.getKey(), entry.getValue());
			}
		}
	}

	protected void addCustomDimension(HitBuilders.EventBuilder eventBuilder, GoogleCoreAnalyticsTracker.CustomDimension customDimension, String dimension) {
		addCustomDimension(eventBuilder, customDimension.name(), dimension);
	}

	protected void addCustomDimension(HitBuilders.EventBuilder eventBuilder, String customDimension, String dimension) {
		Integer index = customDimensionsMap.get(customDimension);
		addCustomDimension(eventBuilder, index, dimension);
	}

	protected void addCustomDimension(HitBuilders.EventBuilder eventBuilder, Integer index, String dimension) {
		if (index != null) {
			LOGGER.debug("Added custom dimension: " + index + " - " + dimension);
			eventBuilder.setCustomDimension(index, dimension);
		}
	}

	protected void addCustomDimension(HitBuilders.TimingBuilder timingBuilder, Integer index, String dimension) {
		if (index != null) {
			LOGGER.debug("Added custom dimension: " + index + " - " + dimension);
			timingBuilder.setCustomDimension(index, dimension);
		}
	}
	protected void addCustomDimension(HitBuilders.TimingBuilder timingBuilder, String customDimensionKey, String dimension) {
		Integer index = customDimensionsMap.get(customDimensionKey);
		addCustomDimension(timingBuilder, index, dimension);
	}

	protected void addCustomDimension(HitBuilders.TransactionBuilder transactionBuilder, Map<String, String> customDimensions) {
		if (customDimensions != null) {
			for (Map.Entry<String, String> entry : customDimensions.entrySet()) {
				addCustomDimension(transactionBuilder, entry.getKey(), entry.getValue());
			}
		}
	}

	protected void addCustomDimension(HitBuilders.TransactionBuilder transactionBuilder, String customDimension, String dimension) {
		Integer index = customDimensionsMap.get(customDimension);
		addCustomDimension(transactionBuilder, index, dimension);
	}

	protected void addCustomDimension(HitBuilders.TransactionBuilder transactionBuilder, Integer index, String dimension) {
		if (index != null) {
			LOGGER.debug("Added custom dimension: " + index + " - " + dimension);
			transactionBuilder.setCustomDimension(index, dimension);
		}
	}

	protected void addCustomMetric(HitBuilders.ScreenViewBuilder screenViewBuilder, String customDimensionKey, Float metric) {
		Integer index = customMetricsMap.get(customDimensionKey);
		addCustomMetric(screenViewBuilder, index, metric);
	}

	protected void addCustomMetric(HitBuilders.ScreenViewBuilder screenViewBuilder, Integer index, Float metric) {
		if (index != null) {
			LOGGER.debug("Added custom metric: " + index + " - " + metric);
			screenViewBuilder.setCustomMetric(index, metric);
		}
	}

	public Boolean isSessionExpired() {
		return (DateUtils.nowMillis() - UsageStats.getLastStopTime()) > (4 * SESSION_TIMEOUT * DateUtils.MILLIS_PER_SECOND);
	}

	public void dispatchLocalHits() {
		GoogleAnalytics.getInstance(AbstractApplication.get()).dispatchLocalHits();
	}

	public void addCommonCustomDimension(String key, String value) {
		commonCustomDimensionsValues.put(key, value);
	}

	public Boolean hasCommonCustomDimension(String key) {
		return commonCustomDimensionsValues.containsKey(key);
	}

	public Executor getExecutor() {
		return executor;
	}
}
