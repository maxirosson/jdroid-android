package com.jdroid.android.sample.google.analytics;

import com.google.android.gms.analytics.HitBuilders;
import com.jdroid.android.google.analytics.AbstractGoogleAnalyticsTracker;
import com.jdroid.android.sample.analytics.AppAnalyticsTracker;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.utils.RandomUtils;

public class AppGoogleAnalyticsTracker extends AbstractGoogleAnalyticsTracker implements AppAnalyticsTracker {

	private static final String EXAMPLE_CATEGORY = "exampleCategory";
	private static final String EXAMPLE_LABEL = "exampleLabel";

	@Override
	public void trackExampleEvent() {
		getGoogleAnalyticsHelper().sendEvent(EXAMPLE_CATEGORY, "exampleAction", EXAMPLE_LABEL);
	}

	@Override
	public void trackExampleTransaction() {
		HitBuilders.TransactionBuilder transactionBuilder = new HitBuilders.TransactionBuilder();
		transactionBuilder.setCurrencyCode("USD");
		transactionBuilder.setRevenue(1000);
		transactionBuilder.setTax(10);
		transactionBuilder.setShipping(5);
		transactionBuilder.setTransactionId("tx" + DateUtils.nowMillis());
		getGoogleAnalyticsHelper().sendTransaction(transactionBuilder);
	}

	@Override
	public void trackExampleTiming() {
		getGoogleAnalyticsHelper().trackTiming(EXAMPLE_CATEGORY, "exampleVariable", EXAMPLE_LABEL, RandomUtils.getLong());
	}

}
