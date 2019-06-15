package com.jdroid.android.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import com.jdroid.android.application.AbstractApplication
import com.jdroid.java.date.DateUtils
import com.jdroid.java.utils.LoggerUtils

object AlarmUtils {

    private val LOGGER = LoggerUtils.getLogger(AlarmUtils::class.java)

    private val alarmManager: AlarmManager
        get() {
            val context = AbstractApplication.get()
            return context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        }

    fun scheduleRtcWakeUpAlarm(triggerAtTime: Long, operation: PendingIntent) {
        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtTime, operation)
        log("RTC_WAKEUP", triggerAtTime)
    }

    fun scheduleRtcAlarm(triggerAtTime: Long, operation: PendingIntent) {
        alarmManager.set(AlarmManager.RTC, triggerAtTime, operation)
        log("RTC", triggerAtTime)
    }

    fun scheduleElapsedRealtimeWakeUpAlarm(triggerAtTime: Long, operation: PendingIntent) {
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, operation)
        log("ELAPSED_REALTIME_WAKEUP", triggerAtTime)
    }

    fun scheduleElapsedRealtimeAlarm(triggerAtTime: Long, operation: PendingIntent) {
        alarmManager.set(AlarmManager.ELAPSED_REALTIME, triggerAtTime, operation)
        log("ELAPSED_REALTIME", triggerAtTime)
    }

    private fun log(alarmType: String, triggerAtTime: Long) {
        LOGGER.debug(
            "Created " + alarmType + " alarm for " +
                DateUtils.formatDateTime(DateUtils.getDate(triggerAtTime))
        )
    }

    fun cancelAlarm(operation: PendingIntent) {
        alarmManager.cancel(operation)
    }
}
