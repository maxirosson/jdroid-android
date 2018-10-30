package com.jdroid.android.firebase.remoteconfig;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.jdroid.android.firebase.fcm.FcmEventsListener;

public class PropagateUpdatesFcmEventsListener implements FcmEventsListener {

	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {

	}

	@Override
	public void onMessageSent(String msgId) {

	}

	@Override
	public void onSendError(String msgId, Exception exception) {

	}

	@Override
	public void onDeletedMessages() {

	}

	@Override
	public void onNewToken(String token) {
		// https://firebase.google.com/docs/remote-config/propagate-updates-realtime
		FirebaseMessaging.getInstance().subscribeToTopic("FIREBASE_RC_PROPAGATION");
	}
}
