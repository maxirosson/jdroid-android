package com.jdroid.android.google.inappbilling;

import android.content.Context;

import com.jdroid.android.debug.DebugSettingsHelper;
import com.jdroid.android.lifecycle.ApplicationLifecycleCallback;

public class InAppBillingDebugAppLifecycleCallback extends ApplicationLifecycleCallback {

	@Override
	public void onProviderInit(Context context) {
		DebugSettingsHelper.addPreferencesAppender(new InAppBillingDebugPrefsAppender());
	}

}
