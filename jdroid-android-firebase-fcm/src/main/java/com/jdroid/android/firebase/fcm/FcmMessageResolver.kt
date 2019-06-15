package com.jdroid.android.firebase.fcm

import com.google.firebase.messaging.RemoteMessage

interface FcmMessageResolver {

    fun resolve(remoteMessage: RemoteMessage): FcmMessage
}
