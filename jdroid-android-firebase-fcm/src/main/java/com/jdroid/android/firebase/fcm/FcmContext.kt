package com.jdroid.android.firebase.fcm

object FcmContext {

    private val fcmMessages = mutableListOf<FcmMessage>()

    private val fcmEventsListeners = mutableListOf<FcmEventsListener>()

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
