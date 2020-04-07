package com.jdroid.android.firebase.fcm.notification

import com.google.firebase.messaging.RemoteMessage
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.firebase.fcm.AbstractFcmMessage
import com.jdroid.android.images.loader.BitmapLoader
import com.jdroid.android.notification.NotificationBuilder
import com.jdroid.android.notification.NotificationUtils
import com.jdroid.java.date.DateUtils
import com.jdroid.java.exception.UnexpectedException
import com.jdroid.java.utils.IdGenerator
import com.jdroid.java.utils.TypeUtils

open class NotificationFcmMessage : AbstractFcmMessage() {

    override fun handle(remoteMessage: RemoteMessage) {

        var channelId: String? = remoteMessage.notification?.channelId
        if (channelId?.isEmpty() == true && remoteMessage.data != null) {
            channelId = remoteMessage.data[CHANNEL]
        }

        val builder = NotificationBuilder(getMessageKey(), channelId)

        initContentTitle(remoteMessage, builder)
        initContentText(remoteMessage, builder)

        initSmallIcon(remoteMessage, builder)
        initLargeIcon(remoteMessage, builder)

        initSound(remoteMessage, builder)
        initVibration(remoteMessage, builder)
        initLight(remoteMessage, builder)

        initContentIntent(remoteMessage, builder)

        builder.setPublicVisibility()
        builder.setWhen(DateUtils.nowMillis())
        configureBuilder(remoteMessage, builder)

        NotificationUtils.sendNotification(IdGenerator.getIntId(), builder)
    }

    protected open fun initContentTitle(remoteMessage: RemoteMessage, builder: NotificationBuilder) {
        var contentTitle: String? = remoteMessage.notification?.title
        if (contentTitle?.isEmpty() == true && remoteMessage.data != null) {
            contentTitle = remoteMessage.data[CONTENT_TITLE]
        }
        if (contentTitle?.isNotEmpty() == true) {
            builder.setContentTitle(contentTitle)
        } else {
            throw UnexpectedException("Missing " + CONTENT_TITLE + " extra for " + getMessageKey())
        }
    }

    protected open fun initContentText(remoteMessage: RemoteMessage, builder: NotificationBuilder) {
        var contentText: String? = remoteMessage.notification?.body
        if (contentText?.isEmpty() == true && remoteMessage.data != null) {
            contentText = remoteMessage.data[CONTENT_TEXT]
        }
        if (contentText?.isNotEmpty() == true) {
            builder.setContentText(contentText)
        } else {
            throw UnexpectedException("Missing " + CONTENT_TEXT + " extra for " + getMessageKey())
        }
    }

    protected open fun initSound(remoteMessage: RemoteMessage, builder: NotificationBuilder) {
        if (remoteMessage.data != null && TypeUtils.getBoolean(remoteMessage.data[SOUND_ENABLED], false)!!) {
            builder.setDefaultSound()
        }
    }

    protected open fun initVibration(remoteMessage: RemoteMessage, builder: NotificationBuilder) {
        if (remoteMessage.data != null && TypeUtils.getBoolean(remoteMessage.data[VIBRATION_ENABLED], false)!!) {
            builder.setDefaultVibration()
        }
    }

    protected open fun initLight(remoteMessage: RemoteMessage, builder: NotificationBuilder) {
        if (remoteMessage.data != null && TypeUtils.getBoolean(remoteMessage.data[LIGHT_ENABLED], false)!!) {
            builder.setWhiteLight()
        }
    }

    protected open fun initContentIntent(remoteMessage: RemoteMessage, builder: NotificationBuilder) {
        var url: String? = remoteMessage.notification?.clickAction
        if (url?.isEmpty() == true && remoteMessage.data != null) {
            url = remoteMessage.data[URL]
        }
        if (url?.isNotEmpty() == true) {
            builder.setSingleTopUrl(url)
        } else {
            throw UnexpectedException("Missing " + URL + " extra for " + getMessageKey())
        }
    }

    protected open fun initSmallIcon(remoteMessage: RemoteMessage, builder: NotificationBuilder) {
        builder.setSmallIcon(AbstractApplication.get().notificationIconResId)
    }

    protected open fun initLargeIcon(remoteMessage: RemoteMessage, builder: NotificationBuilder) {
        if (remoteMessage.data != null) {
            val largeIconUrl = remoteMessage.data[LARGE_ICON_URL]
            val bitmapLoader = createBitmapLoader(largeIconUrl)
            if (largeIconUrl != null) {
                if (bitmapLoader != null) {
                    builder.setLargeIcon(bitmapLoader)
                } else {
                    AbstractApplication.get().exceptionHandler.logWarningException("Not bitmapLoader defined to load large icon url")
                }
            }
        }
    }

    protected open fun createBitmapLoader(url: String?): BitmapLoader? {
        return null
    }

    protected open fun configureBuilder(remoteMessage: RemoteMessage, notificationBuilder: NotificationBuilder) {
        // Do Nothing
    }

    override fun getMessageKey(): String {
        return MESSAGE_KEY
    }

    companion object {
        const val CHANNEL = "channel"
        const val CONTENT_TITLE = "contentTitle"
        const val CONTENT_TEXT = "contentText"
        const val SOUND_ENABLED = "soundEnabled"
        const val VIBRATION_ENABLED = "vibrationEnabled"
        const val LIGHT_ENABLED = "lightEnabled"
        const val URL = "url"
        const val LARGE_ICON_URL = "largeIconUrl"
        const val MESSAGE_KEY = "notificationFcmMessage"
    }
}
