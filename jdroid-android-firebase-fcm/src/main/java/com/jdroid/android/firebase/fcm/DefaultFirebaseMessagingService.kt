package com.jdroid.android.firebase.fcm

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Service to handle FCM messages.
 */
class DefaultFirebaseMessagingService : FirebaseMessagingService() {

    /*
	 * Since Android O, have a guaranteed life cycle limited to 10 seconds for this method execution.
	 * To avoid your process being terminated before your callback is completed, be sure to perform only quick operations
	 * (like updating a local database, or displaying a custom notification) inside the callback, and use JobScheduler to
	 * schedule longer background processes (like downloading additional images or syncing the database with a remote source).
	 */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        AbstractFcmAppModule.get().fcmListenerResolver.onMessageReceived(remoteMessage)
    }

    override fun onMessageSent(msgId: String) {
        AbstractFcmAppModule.get().fcmListenerResolver.onMessageSent(msgId)
    }

    override fun onSendError(msgId: String, exception: Exception) {
        AbstractFcmAppModule.get().fcmListenerResolver.onSendError(msgId, exception)
    }

    override fun onDeletedMessages() {
        AbstractFcmAppModule.get().fcmListenerResolver.onDeletedMessages()
    }

    // Called when a new token for the default Firebase project is generated.
    // This may occur if the security of the previous token had been compromised
    // This is invoked after app install when a token is first generated, and again if the token changes.
    override fun onNewToken(token: String) {
        AbstractFcmAppModule.get().fcmListenerResolver.onNewToken(token)
    }
}
