package com.jdroid.android.sample.debug;

import android.content.Context;

import com.jdroid.android.firebase.fcm.AbstractFcmDebugAppLifecycleCallback;
import com.jdroid.android.firebase.fcm.FcmDebugPrefsAppender;
import com.jdroid.android.sample.firebase.fcm.SampleFcmMessage;

public class FcmDebugAppLifecycleCallback extends AbstractFcmDebugAppLifecycleCallback {

	@Override
	public void onProviderInit(Context context) {
		super.onProviderInit(context);
		FcmDebugPrefsAppender.addFcmMessage(new SampleFcmMessage(), null);
	}
}
