package com.jdroid.android.firebase.admob;

import android.content.Context;

import com.jdroid.android.lifecycle.ApplicationLifecycleCallback;
import com.jdroid.android.firebase.remoteconfig.FirebaseRemoteConfigLoader;


public class AdMobAppLifecycleCallback extends ApplicationLifecycleCallback {
	
	@Override
	public void onProviderInit(Context context) {
		FirebaseRemoteConfigLoader.get().addRemoteConfigParameters(AdMobRemoteConfigParameter.values());
	}
}
