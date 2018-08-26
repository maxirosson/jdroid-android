package com.jdroid.android.sample.firebase.fcm;


import com.jdroid.android.firebase.fcm.AbstractFcmMessageResolver;

public class AndroidFcmResolver extends AbstractFcmMessageResolver {

	public AndroidFcmResolver() {
		super(AndroidFcmMessage.values());
	}

	@Override
	protected void onNotAuthenticatedUser(Long userId) {
	}

}
