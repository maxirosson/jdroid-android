package com.jdroid.android.firebase.fcm;

import com.google.firebase.messaging.RemoteMessage;

public interface FcmEventsListener {

	void onMessageReceived(RemoteMessage remoteMessage);

	void onMessageSent(String msgId);

	void onSendError(String msgId, Exception exception);

	void onDeletedMessages();

	void onNewToken(String token);
}
