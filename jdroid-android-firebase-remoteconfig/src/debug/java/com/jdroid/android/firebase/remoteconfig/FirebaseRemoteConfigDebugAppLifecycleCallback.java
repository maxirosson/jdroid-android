package com.jdroid.android.firebase.remoteconfig;

import android.content.Context;
import android.support.annotation.NonNull;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.debug.DebugSettingsHelper;
import com.jdroid.android.lifecycle.ApplicationLifecycleCallback;

public class FirebaseRemoteConfigDebugAppLifecycleCallback extends ApplicationLifecycleCallback {
	
	@Override
	public void onProviderInit(Context context) {
		
		if (MockRemoteConfigLoader.isMocksEnabled()) {
			AbstractApplication.get().setRemoteConfigLoader(new MockRemoteConfigLoader(true));
		}
		
		DebugSettingsHelper.addPreferencesAppender(new FirebaseRemoteConfigPrefsAppender());
	}
	
	@NonNull
	@Override
	public Integer getInitOrder() {
		return 1;
	}
	
}
