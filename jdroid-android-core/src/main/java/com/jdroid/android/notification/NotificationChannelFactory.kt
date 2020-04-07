package com.jdroid.android.notification

import android.app.NotificationChannel
import android.os.Build
import androidx.annotation.RequiresApi

object NotificationChannelFactory {

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun createNotificationChannel(notificationChannelType: NotificationChannelType): NotificationChannel {
        val notificationChannel = NotificationChannel(
            notificationChannelType.getChannelId(),
            notificationChannelType.getUserVisibleName(),
            notificationChannelType.getImportance()
        )
        notificationChannelType.config(notificationChannel)
        return notificationChannel
    }
}
