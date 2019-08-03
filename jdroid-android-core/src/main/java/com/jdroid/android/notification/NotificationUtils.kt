package com.jdroid.android.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.utils.ScreenUtils

object NotificationUtils {

    private val notificationManager: NotificationManager by lazy {
        AbstractApplication.get().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private val notificationChannelTypes = mutableListOf<NotificationChannelType>()

    val notificationLargeIconWidthPx: Int
        get() = ScreenUtils.convertDimenToPixel(android.R.dimen.notification_large_icon_width)

    val notificationLargeIconHeightPx: Int
        get() = ScreenUtils.convertDimenToPixel(android.R.dimen.notification_large_icon_height)

    fun sendNotification(id: Int, notificationBuilder: NotificationBuilder) {
        sendNotification(id, notificationBuilder.build())
    }

    fun sendNotification(id: Int, notification: Notification?) {
        if (notification != null) {
            notificationManager.notify(id, notification)
        }
    }

    fun cancelNotification(id: Int) {
        notificationManager.cancel(id)
    }

    /**
     * Cancel all previously shown notifications.
     */
    fun cancelAllNotifications() {
        notificationManager.cancelAll()
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun createNotificationChannel(notificationChannel: NotificationChannel) {
        notificationManager.createNotificationChannel(notificationChannel)
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun createNotificationChannels(notificationChannels: List<NotificationChannel>) {
        notificationManager.createNotificationChannels(notificationChannels)
    }

    fun createNotificationChannelsByType(notificationChannelTypes: List<NotificationChannelType>) {
        this.notificationChannelTypes.clear()
        this.notificationChannelTypes.addAll(notificationChannelTypes)
        for (notificationChannelType in notificationChannelTypes) {
            if (notificationChannelType.isDeprecated()) {
                this.notificationChannelTypes.remove(notificationChannelType)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationManager.deleteNotificationChannel(notificationChannelType.getChannelId())
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    createNotificationChannel(NotificationChannelFactory.createNotificationChannel(notificationChannelType))
                }
            }
        }
    }

    fun findNotificationChannelType(channelId: String?): NotificationChannelType? {
        for (notificationChannelType in notificationChannelTypes) {
            if (notificationChannelType.getChannelId() == channelId) {
                return notificationChannelType
            }
        }
        return null
    }
}