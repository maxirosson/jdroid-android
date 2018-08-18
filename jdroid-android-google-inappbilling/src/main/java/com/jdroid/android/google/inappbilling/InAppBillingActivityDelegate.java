package com.jdroid.android.google.inappbilling;

import android.os.Bundle;

import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.activity.ActivityDelegate;
import com.jdroid.android.google.inappbilling.client.InAppBillingBroadcastReceiver;
import com.jdroid.android.google.inappbilling.ui.InAppBillingHelperFragment;

public class InAppBillingActivityDelegate extends ActivityDelegate {

	private static Boolean inAppBillingLoaded = false;

	private InAppBillingBroadcastReceiver broadcastReceiver;

	public InAppBillingActivityDelegate(AbstractFragmentActivity activity) {
		super(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if ((savedInstanceState == null) && InAppBillingAppModule.get().isInAppBillingEnabled() && !inAppBillingLoaded) {
			InAppBillingHelperFragment.add(getActivity(), InAppBillingHelperFragment.class, true, null);
			inAppBillingLoaded = true;
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		if (InAppBillingAppModule.get().isInAppBillingEnabled()) {
			// Important: Dynamically register for broadcast messages about updated purchases.
			// We register the receiver here instead of as a <receiver> in the Manifest
			// because we always call getPurchases() at startup, so therefore we can ignore
			// any broadcasts sent while the app isn't running.
			broadcastReceiver = new InAppBillingBroadcastReceiver();
			getActivity().registerReceiver(broadcastReceiver, broadcastReceiver.createIntentFilter());
		}
	}

	@Override
	public void onPause() {
		super.onPause();

		if (broadcastReceiver != null) {
			getActivity().unregisterReceiver(broadcastReceiver);
		}
	}
}
