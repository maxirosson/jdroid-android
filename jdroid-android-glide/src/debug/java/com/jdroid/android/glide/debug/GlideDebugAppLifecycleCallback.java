package com.jdroid.android.glide.debug;

import android.content.Context;

import com.jdroid.android.debug.DebugSettingsHelper;
import com.jdroid.android.lifecycle.ApplicationLifecycleCallback;

public class GlideDebugAppLifecycleCallback extends ApplicationLifecycleCallback {

	@Override
	public void onProviderInit(Context context) {
		super.onProviderInit(context);

		DebugSettingsHelper.addPreferencesAppender(new GlideDebugPrefsAppender());
	}
}
