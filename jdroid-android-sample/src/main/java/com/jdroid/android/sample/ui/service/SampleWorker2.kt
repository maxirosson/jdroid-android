package com.jdroid.android.sample.ui.service

import android.content.Context
import androidx.work.WorkerParameters
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.jetpack.work.AbstractWorker
import com.jdroid.android.notification.NotificationBuilder
import com.jdroid.android.notification.NotificationUtils
import com.jdroid.android.sample.application.AndroidNotificationChannelType
import com.jdroid.java.http.exception.ConnectionException
import com.jdroid.java.utils.IdGenerator

class SampleWorker2(context: Context, workerParams: WorkerParameters) : AbstractWorker(context, workerParams) {

    override fun onWork(): Result {
        val fail = inputData.getBoolean("fail")
        if (fail) {
            throw ConnectionException("Failing service")
        } else {
            val builder = NotificationBuilder("myNotification", AndroidNotificationChannelType.DEFAULT_IMPORTANCE)
            builder.setSmallIcon(AbstractApplication.get().notificationIconResId)
            builder.setTicker("Sample Ticker")
            builder.setContentTitle(javaClass.simpleName)
            builder.setContentText(inputData.getString("a"))

            NotificationUtils.sendNotification(IdGenerator.getIntId(), builder)
            return Result.success()
        }
    }
}