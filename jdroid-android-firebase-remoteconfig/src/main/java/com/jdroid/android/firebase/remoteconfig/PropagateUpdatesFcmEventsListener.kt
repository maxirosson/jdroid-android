package com.jdroid.android.firebase.remoteconfig

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import com.jdroid.android.firebase.fcm.FcmEventsListener

class PropagateUpdatesFcmEventsListener : FcmEventsListener {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
    }

    override fun onMessageSent(msgId: String) {
    }

    override fun onSendError(msgId: String, exception: Exception) {
    }

    override fun onDeletedMessages() {
    }

    override fun onNewToken(token: String) {
        // https://firebase.google.com/docs/remote-config/propagate-updates-realtime
        FirebaseMessaging.getInstance().subscribeToTopic("FIREBASE_RC_PROPAGATION")
    }
}
