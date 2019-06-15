package com.jdroid.android.firebase.fcm

import com.google.firebase.messaging.RemoteMessage

interface FcmMessage {

    fun matches(remoteMessage: RemoteMessage): Boolean

    /**
     * Since Android O, have a guaranteed life cycle limited to 10 seconds for this method execution
     *
     * @param remoteMessage
     */
    fun handle(remoteMessage: RemoteMessage)
}
