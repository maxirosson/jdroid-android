package com.jdroid.android.firebase.fcm

import com.google.firebase.messaging.RemoteMessage
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.auth.SecurityContext
import com.jdroid.android.firebase.fcm.notification.NotificationFcmMessage
import com.jdroid.android.utils.AndroidUtils
import com.jdroid.android.utils.AppUtils
import com.jdroid.java.utils.LoggerUtils
import com.jdroid.java.utils.TypeUtils

open class DefaultFcmMessageResolver : FcmMessageResolver {
    init {

        val notificationFcmMessage = createNotificationFcmMessage()
        if (notificationFcmMessage != null) {
            FcmContext.addFcmMessage(notificationFcmMessage)
        }
    }

    protected fun createNotificationFcmMessage(): NotificationFcmMessage? {
        return NotificationFcmMessage()
    }

    override fun resolve(remoteMessage: RemoteMessage): FcmMessage? {
        LOGGER.debug("FCM message received. " + remoteMessage.data)
        val minAppVersionCode = TypeUtils.getLong(remoteMessage.data[MIN_APP_VERSION_CODE_KEY], 0L)
        if (AppUtils.getVersionCode() >= minAppVersionCode) {
            val minDeviceOsVersion = TypeUtils.getLong(remoteMessage.data[MIN_DEVICE_OS_VERSION_KEY], 0L)
            if (AndroidUtils.getApiLevel() >= minDeviceOsVersion) {
                for (each in FcmContext.getFcmMessages()) {
                    if (each.matches(remoteMessage)) {

                        val userId = TypeUtils.getLong(remoteMessage.data[USER_ID_KEY])

                        // We should ignore messages received for previously logged users
                        if (userId != null && (!SecurityContext.isAuthenticated() || SecurityContext.user!!.id != userId)) {
                            LOGGER.warn("The FCM message is ignored because it was sent to another user: $userId")
                            onNotAuthenticatedUser(userId)
                            return null
                        }
                        return each
                    }
                }
                AbstractApplication.get().exceptionHandler.logWarningException("Ignoring FCM message. Not resolved")
            } else {
                LOGGER.debug("Ignoring FCM message. minDeviceOsVersion: " + minDeviceOsVersion!!)
            }
        } else {
            LOGGER.debug("Ignoring FCM message. minAppVersionCode: " + minAppVersionCode!!)
        }
        return null
    }

    protected open fun onNotAuthenticatedUser(userId: Long?) {
        // Do nothing
    }

    companion object {

        private val LOGGER = LoggerUtils.getLogger(DefaultFcmMessageResolver::class.java)

        const val USER_ID_KEY = "userIdKey"
        const val MIN_DEVICE_OS_VERSION_KEY = "minDeviceOsVersion"
        const val MIN_APP_VERSION_CODE_KEY = "minAppVersionCode"
    }
}
