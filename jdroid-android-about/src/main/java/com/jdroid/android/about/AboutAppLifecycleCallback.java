package com.jdroid.android.about;

import android.content.Context;

import com.jdroid.android.firebase.remoteconfig.FirebaseRemoteConfigHelper;
import com.jdroid.android.lifecycle.ApplicationLifecycleCallback;


public class AboutAppLifecycleCallback extends ApplicationLifecycleCallback {
	
	@Override
	public void onProviderInit(Context context) {
		
		FirebaseRemoteConfigHelper.addRemoteConfigParameters(AboutRemoteConfigParameter.values());
	}
}
