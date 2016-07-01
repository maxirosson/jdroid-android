package com.jdroid.android.google.inappbilling;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.jdroid.android.application.AbstractApplication;

public class InAppBillingHelper {

	private InAppBillingHelper(){}

	private static Boolean inAppBillingLoaded = false;

	public static void onCreate(FragmentActivity activity, Bundle savedInstanceState) {

		if ((savedInstanceState == null) && InAppBillingAppModule.get().getInAppBillingContext() != null && !inAppBillingLoaded) {
			InAppBillingHelperFragment.add(activity, InAppBillingHelperFragment.class, true, null);
			inAppBillingLoaded = true;
		}
	}
}
