package com.jdroid.android.firebase.fcm;

import com.google.firebase.messaging.RemoteMessage;

public abstract class AbstractFcmMessage implements FcmMessage {

	public static final String MESSAGE_KEY_EXTRA = "messageKey";

	public abstract String getMessageKey();

	@Override
	public Boolean matches(RemoteMessage remoteMessage) {
		String messageKey = remoteMessage.getData().get(getMessageKeyExtraName());
		return getMessageKey().equalsIgnoreCase(messageKey);
	}

	protected String getMessageKeyExtraName() {
		return MESSAGE_KEY_EXTRA;
	}
}
