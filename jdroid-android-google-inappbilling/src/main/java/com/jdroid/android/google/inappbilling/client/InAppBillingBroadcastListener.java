package com.jdroid.android.google.inappbilling.client;

import android.support.annotation.MainThread;

/**
 * Listener interface for received broadcast messages.
 */
public interface InAppBillingBroadcastListener {
	
	// TODO Your onReceive() method should respond to the intent by calling getPurchases()
	@MainThread
	void onPurchasesUpdated();
}
