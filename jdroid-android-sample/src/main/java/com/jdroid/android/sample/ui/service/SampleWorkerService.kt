package com.jdroid.android.sample.ui.service

import android.content.Intent

import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.kotlin.getRequiredString
import com.jdroid.android.kotlin.requireExtras
import com.jdroid.android.notification.NotificationBuilder
import com.jdroid.android.notification.NotificationUtils
import com.jdroid.android.sample.application.AndroidNotificationChannelType
import com.jdroid.android.service.AbstractWorkerService
import com.jdroid.java.exception.UnexpectedException
import com.jdroid.java.utils.IdGenerator

class SampleWorkerService : AbstractWorkerService() {

    override fun doExecute(intent: Intent) {

        val fail = intent.requireExtras().getBoolean("fail")

        if (fail) {
            throw UnexpectedException("Failing service")
        } else {
            val builder = NotificationBuilder("myNotification", AndroidNotificationChannelType.DEFAULT_IMPORTANCE)
            builder.setSmallIcon(AbstractApplication.get().notificationIconResId)
            builder.setTicker("Sample Ticker")
            builder.setContentTitle(javaClass.simpleName)
            builder.setContentText(intent.requireExtras().getRequiredString("a"))

            NotificationUtils.sendNotification(IdGenerator.getIntId(), builder)
        }
    }

    companion object {
        fun runIntentInService(intent: Intent) {
            runIntentInService(AbstractApplication.get(), intent, SampleWorkerService::class.java)
        }
    }
}
