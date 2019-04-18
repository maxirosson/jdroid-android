package com.jdroid.android.about;

import android.content.Context;

import com.jdroid.android.debug.DebugSettingsHelper;
import com.jdroid.android.lifecycle.ApplicationLifecycleCallback;

public class AboutDebugAppLifecycleCallback extends ApplicationLifecycleCallback {

	@Override
	public void onProviderInit(Context context) {
		DebugSettingsHelper.addPreferencesAppender(new RateAppDebugPrefsAppender());
	}
}
