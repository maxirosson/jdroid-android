package com.jdroid.android.google.inappbilling;

import android.os.Bundle;

import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.activity.ActivityDelegate;
import com.jdroid.android.google.inappbilling.ui.InAppBillingHelperFragment;

public class InAppBillingActivityDelegate extends ActivityDelegate {

	private static Boolean inAppBillingLoaded = false;

	public InAppBillingActivityDelegate(AbstractFragmentActivity activity) {
		super(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if ((savedInstanceState == null) && !inAppBillingLoaded) {
			InAppBillingHelperFragment.add(getActivity(), InAppBillingHelperFragment.class, true, null);
			inAppBillingLoaded = true;
		}
	}
}
