package com.jdroid.android.firebase.fcm

import com.google.firebase.messaging.RemoteMessage

abstract class AbstractFcmMessage : FcmMessage {

    companion object {
        const val MESSAGE_KEY_EXTRA = "messageKey"
    }

    abstract fun getMessageKey(): String

    override fun matches(remoteMessage: RemoteMessage): Boolean {
        val messageKey = remoteMessage.data[getMessageKeyExtraName()]
        return getMessageKey().equals(messageKey, ignoreCase = true)
    }

    protected fun getMessageKeyExtraName(): String {
        return MESSAGE_KEY_EXTRA
    }
}
