package com.jdroid.android.firebase.fcm;

import android.content.Context;

import com.jdroid.android.lifecycle.ApplicationLifecycleCallback;

public class FcmApplicationLifecycleCallback extends ApplicationLifecycleCallback {

	@Override
	public void onLocaleChanged(Context context) {
		AbstractFcmAppModule.get().startFcmRegistration(false);
	}
}
