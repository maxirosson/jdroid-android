package com.jdroid.android.firebase.fcm

import com.google.firebase.messaging.RemoteMessage

interface FcmEventsListener {

    fun onMessageReceived(remoteMessage: RemoteMessage)

    fun onMessageSent(msgId: String)

    fun onSendError(msgId: String, exception: Exception)

    fun onDeletedMessages()

    fun onNewToken(token: String)
}
