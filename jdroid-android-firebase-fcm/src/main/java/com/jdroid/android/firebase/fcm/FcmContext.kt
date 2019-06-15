package com.jdroid.android.firebase.fcm

import com.jdroid.java.collections.Lists

object FcmContext {

    private val fcmMessages = Lists.newArrayList<FcmMessage>()

    private val fcmEventsListeners = Lists.newArrayList<FcmEventsListener>()

    fun addFcmMessage(fcmMessage: FcmMessage) {
        fcmMessages.add(fcmMessage)
    }

    fun getFcmMessages(): List<FcmMessage> {
        return fcmMessages
    }

    fun getFcmEventsListeners(): List<FcmEventsListener> {
        return fcmEventsListeners
    }

    fun addFcmEventsListener(fcmEventsListener: FcmEventsListener) {
        fcmEventsListeners.add(fcmEventsListener)
    }
}
