package com.jdroid.android.about;

import android.content.Context;

import com.jdroid.android.firebase.remoteconfig.FirebaseRemoteConfigLoader;
import com.jdroid.android.lifecycle.ApplicationLifecycleCallback;


public class AboutAppLifecycleCallback extends ApplicationLifecycleCallback {
	
	@Override
	public void onProviderInit(Context context) {
		FirebaseRemoteConfigLoader.get().addRemoteConfigParameters(AboutRemoteConfigParameter.values());
	}
}
