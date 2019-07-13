package com.jdroid.android.firebase.fcm.kotlin

import com.google.firebase.messaging.RemoteMessage

fun RemoteMessage.getRequiredPayload(key: String): String {
    return this.data[key]!!
}

fun RemoteMessage.getPayload(key: String): String? {
    return this.data[key]
}
