package com.jdroid.android.firebase.admob;

import android.content.Context;

import com.jdroid.android.lifecycle.ApplicationLifecycleCallback;
import com.jdroid.android.firebase.remoteconfig.FirebaseRemoteConfigHelper;


public class AdMobAppLifecycleCallback extends ApplicationLifecycleCallback {
	
	@Override
	public void onProviderInit(Context context) {
		FirebaseRemoteConfigHelper.get().addRemoteConfigParameters(AdMobRemoteConfigParameter.values());
	}
}
