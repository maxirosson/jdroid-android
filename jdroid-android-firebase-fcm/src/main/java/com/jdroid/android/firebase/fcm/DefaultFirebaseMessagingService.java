package com.jdroid.android.firebase.fcm;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


/**
 * Service to handle FCM messages.
 */
public class DefaultFirebaseMessagingService extends FirebaseMessagingService {

	/*
	 * Since Android O, have a guaranteed life cycle limited to 10 seconds for this method execution.
	 * To avoid your process being terminated before your callback is completed, be sure to perform only quick operations
	 * (like updating a local database, or displaying a custom notification) inside the callback, and use JobScheduler to
	 * schedule longer background processes (like downloading additional images or syncing the database with a remote source).
	 */
	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {
		AbstractFcmAppModule.get().getFcmListenerResolver().onMessageReceived(remoteMessage);
	}

	@Override
	public void onMessageSent(String msgId) {
		AbstractFcmAppModule.get().getFcmListenerResolver().onMessageSent(msgId);
	}

	@Override
	public void onSendError(String msgId, Exception exception) {
		AbstractFcmAppModule.get().getFcmListenerResolver().onSendError(msgId, exception);
	}

	@Override
	public void onDeletedMessages() {
		AbstractFcmAppModule.get().getFcmListenerResolver().onDeletedMessages();
	}
	
	// Called when a new token for the default Firebase project is generated.
	// This may occur if the security of the previous token had been compromised
	// This is invoked after app install when a token is first generated, and again if the token changes.
	@Override
	public void onNewToken(String token) {
		AbstractFcmAppModule.get().getFcmListenerResolver().onNewToken(token);
	}
}
