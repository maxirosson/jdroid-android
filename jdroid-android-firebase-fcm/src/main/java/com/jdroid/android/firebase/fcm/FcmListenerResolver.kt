package com.jdroid.android.firebase.fcm

import com.google.firebase.messaging.RemoteMessage
import com.jdroid.android.application.AbstractApplication
import com.jdroid.java.utils.LoggerUtils

class FcmListenerResolver {

    /**
     * Called when message is received. Since Android O, have a guaranteed life cycle limited to 10 seconds for this method execution
     */
    fun onMessageReceived(remoteMessage: RemoteMessage) {

        if (LoggerUtils.isEnabled()) {
            val builder = StringBuilder()
            builder.append("Message received. ")
            builder.append("from: ")
            builder.append(remoteMessage.from)

            if (remoteMessage.messageType != null) {
                builder.append(", ")
                builder.append("message type: ")
                builder.append(remoteMessage.messageType)
            }

            if ((remoteMessage.data != null) and remoteMessage.data.isNotEmpty()) {
                builder.append(", ")
                builder.append("data: ")
                builder.append(remoteMessage.data)
            }
            LOGGER.info(builder.toString())
        }

        val fcmResolver = AbstractFcmAppModule.get().fcmMessageResolver
        var fcmMessage: FcmMessage?
        try {
            fcmMessage = fcmResolver.resolve(remoteMessage)
        } catch (e: Exception) {
            AbstractApplication.get().exceptionHandler.logHandledException("Error when resolving FCM message", e)
            return
        }

        if (fcmMessage != null) {
            try {
                fcmMessage.handle(remoteMessage)
            } catch (e: Exception) {
                AbstractApplication.get().exceptionHandler.logHandledException("Error when handling FCM message", e)
            }
        } else {
            LOGGER.warn("The FCM message was not resolved")
        }

        for (fcmEventsListener in FcmContext.getFcmEventsListeners()) {
            try {
                fcmEventsListener.onMessageReceived(remoteMessage)
            } catch (e: Exception) {
                AbstractApplication.get().exceptionHandler.logHandledException(e)
            }
        }
    }

    fun onMessageSent(msgId: String) {
        LOGGER.info("Message sent with id: $msgId")

        for (fcmEventsListener in FcmContext.getFcmEventsListeners()) {
            try {
                fcmEventsListener.onMessageSent(msgId)
            } catch (e: Exception) {
                AbstractApplication.get().exceptionHandler.logHandledException(e)
            }
        }
    }

    fun onSendError(msgId: String, exception: Exception) {
        AbstractApplication.get().exceptionHandler.logWarningException("Send error. Message id: $msgId", exception)

        for (fcmEventsListener in FcmContext.getFcmEventsListeners()) {
            try {
                fcmEventsListener.onSendError(msgId, exception)
            } catch (e: Exception) {
                AbstractApplication.get().exceptionHandler.logHandledException(e)
            }
        }
    }

    fun onDeletedMessages() {
        LOGGER.info("Deleted messages")

        for (fcmEventsListener in FcmContext.getFcmEventsListeners()) {
            try {
                fcmEventsListener.onDeletedMessages()
            } catch (e: Exception) {
                AbstractApplication.get().exceptionHandler.logHandledException(e)
            }
        }
    }

    fun onNewToken(token: String) {
        LOGGER.info("New token: $token")

        for (fcmEventsListener in FcmContext.getFcmEventsListeners()) {
            try {
                fcmEventsListener.onNewToken(token)
            } catch (e: Exception) {
                AbstractApplication.get().exceptionHandler.logHandledException(e)
            }
        }

        AbstractFcmAppModule.get().startFcmRegistration(false)
    }

    companion object {
        private val LOGGER = LoggerUtils.getLogger(FcmListenerResolver::class.java)
    }
}
