package com.jdroid.android.about;

import android.content.Context;

import com.jdroid.android.lifecycle.ApplicationLifecycleCallback;
import com.jdroid.android.debug.DebugSettingsHelper;

public class AboutDebugAppLifecycleCallback extends ApplicationLifecycleCallback {
	
	@Override
	public void onProviderInit(Context context) {
		DebugSettingsHelper.addPreferencesAppender(new AppInviteDebugPrefsAppender());
		DebugSettingsHelper.addPreferencesAppender(new RateAppDebugPrefsAppender());
	}
}
