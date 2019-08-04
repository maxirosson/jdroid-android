package com.jdroid.android.sample.ui.service

import android.content.Context
import android.os.Bundle

import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.kotlin.getRequiredString
import com.jdroid.android.notification.NotificationBuilder
import com.jdroid.android.notification.NotificationUtils
import com.jdroid.android.sample.application.AndroidNotificationChannelType
import com.jdroid.java.exception.UnexpectedException
import com.jdroid.java.utils.IdGenerator

class SampleServiceCommand1 : AbstractSampleServiceCommand() {

    override fun execute(context: Context, bundle: Bundle): Boolean {
        val fail = bundle.getBoolean("fail")
        if (fail) {
            throw UnexpectedException("Failing service")
        } else {
            val builder = NotificationBuilder("myNotification", AndroidNotificationChannelType.DEFAULT_IMPORTANCE)
            builder.setSmallIcon(AbstractApplication.get().notificationIconResId)
            builder.setTicker("Sample Ticker")
            builder.setContentTitle(javaClass.simpleName)
            builder.setContentText(bundle.getRequiredString("a"))

            NotificationUtils.sendNotification(IdGenerator.getIntId(), builder)
            return false
        }
    }
}
