package com.jdroid.android.sample.google.inappbilling;

import android.support.annotation.MainThread;

import com.jdroid.android.google.inappbilling.client.InAppBillingBroadcastListener;
import com.jdroid.java.utils.LoggerUtils;

public class SampleInAppBillingBroadcastListener implements InAppBillingBroadcastListener {
	
	@MainThread
	@Override
	public void onPurchasesUpdated() {
		LoggerUtils.getLogger(SampleInAppBillingBroadcastListener.class).debug(SampleInAppBillingBroadcastListener.class.getSimpleName() + " executed");
	}
}
