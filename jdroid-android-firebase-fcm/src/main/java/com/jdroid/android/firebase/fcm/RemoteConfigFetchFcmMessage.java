package com.jdroid.android.firebase.fcm;

import com.google.firebase.messaging.RemoteMessage;
import com.jdroid.android.application.AbstractApplication;

public class RemoteConfigFetchFcmMessage implements FcmMessage {

	private static final String MESSAGE_KEY = "remoteConfigFetchFcmMessage";

	@Override
	public String getMessageKey() {
		return MESSAGE_KEY;
	}

	@Override
	public void handle(RemoteMessage remoteMessage) {
		AbstractApplication.get().getRemoteConfigLoader().fetch();
	}
}
