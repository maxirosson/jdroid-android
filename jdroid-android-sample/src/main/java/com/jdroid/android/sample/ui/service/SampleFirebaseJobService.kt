package com.jdroid.android.sample.ui.service

import android.os.Bundle
import androidx.annotation.MainThread
import com.firebase.jobdispatcher.FirebaseJobDispatcher
import com.firebase.jobdispatcher.GooglePlayDriver
import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.Lifetime
import com.firebase.jobdispatcher.RetryStrategy
import com.firebase.jobdispatcher.Trigger
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.firebase.jobdispatcher.AbstractJobService
import com.jdroid.android.kotlin.getRequiredString
import com.jdroid.android.notification.NotificationBuilder
import com.jdroid.android.notification.NotificationUtils
import com.jdroid.android.sample.application.AndroidNotificationChannelType
import com.jdroid.java.exception.UnexpectedException
import com.jdroid.java.utils.IdGenerator

class SampleFirebaseJobService : AbstractJobService() {

    @MainThread
    override fun onRunJob(jobParameters: JobParameters): Boolean {
        val fail = jobParameters.extras!!.getBoolean("fail")
        if (fail) {
            throw UnexpectedException("Failing service")
        } else {
            val builder = NotificationBuilder("myNotification", AndroidNotificationChannelType.DEFAULT_IMPORTANCE)
            builder.setSmallIcon(AbstractApplication.get().notificationIconResId)
            builder.setTicker("Sample Ticker")
            builder.setContentTitle(javaClass.simpleName)
            builder.setContentText(jobParameters.extras!!.getRequiredString("a"))

            NotificationUtils.sendNotification(IdGenerator.getIntId(), builder)
            return false
        }
    }

    companion object {

        fun runIntentInService(bundle: Bundle) {
            val dispatcher = FirebaseJobDispatcher(GooglePlayDriver(AbstractApplication.get()))
            val builder = dispatcher.newJobBuilder()
            builder.isRecurring = false // one-off job
            builder.lifetime = Lifetime.FOREVER
            builder.tag = SampleFirebaseJobService::class.java.simpleName
            builder.setService(SampleFirebaseJobService::class.java)
            builder.trigger = Trigger.executionWindow(0, 5)
            builder.setReplaceCurrent(false)
            builder.retryStrategy = RetryStrategy.DEFAULT_EXPONENTIAL
            builder.extras = bundle
            dispatcher.mustSchedule(builder.build())
        }
    }
}
