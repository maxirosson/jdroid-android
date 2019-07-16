package com.jdroid.android.sample.firebase.fcm

import com.google.firebase.messaging.RemoteMessage
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.firebase.fcm.AbstractFcmMessage
import com.jdroid.android.notification.NotificationBuilder
import com.jdroid.android.notification.NotificationUtils
import com.jdroid.android.sample.application.AndroidNotificationChannelType
import com.jdroid.java.date.DateTimeFormat
import com.jdroid.java.date.DateUtils
import com.jdroid.java.utils.IdGenerator
import com.jdroid.java.utils.TypeUtils

import java.util.Date

class SampleFcmMessage : AbstractFcmMessage() {

    override fun getMessageKey(): String {
        return "sampleMessage"
    }

    override fun handle(remoteMessage: RemoteMessage) {
        val builder = NotificationBuilder("pushNotification", AndroidNotificationChannelType.DEFAULT_IMPORTANCE)
        builder.setSmallIcon(AbstractApplication.get().notificationIconResId)
        builder.setTicker("Sample Ticker")
        builder.setContentTitle("Sample Content Title")
        var description: String? = "Sample Content Description"
        val timestamp = TypeUtils.getLong(remoteMessage.data["timestamp"])
        if (timestamp != null) {
            description = DateUtils.format(Date(timestamp), DateTimeFormat.YYYYMMDDHHMMSSSSS)
        }
        builder.setContentText(description)
        builder.setWhen(DateUtils.nowMillis())

        NotificationUtils.sendNotification(IdGenerator.getIntId(), builder)
    }
}
