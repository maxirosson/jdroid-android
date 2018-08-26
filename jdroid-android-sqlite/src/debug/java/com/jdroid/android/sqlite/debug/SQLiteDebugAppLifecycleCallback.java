package com.jdroid.android.sqlite.debug;

import android.content.Context;

import com.jdroid.android.debug.DebugSettingsHelper;
import com.jdroid.android.lifecycle.ApplicationLifecycleCallback;

public class SQLiteDebugAppLifecycleCallback extends ApplicationLifecycleCallback {

	@Override
	public void onProviderInit(Context context) {
		DebugSettingsHelper.addPreferencesAppender(new DatabaseDebugPrefsAppender());
	}
}
