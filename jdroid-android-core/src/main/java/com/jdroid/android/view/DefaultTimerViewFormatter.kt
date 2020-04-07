package com.jdroid.android.view

import java.util.Locale
import java.util.concurrent.TimeUnit

class DefaultTimerViewFormatter : TimerViewFormatter {

    override fun formatDuration(duration: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(duration)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(hours)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minutes)

        val builder = StringBuilder()
        if (hours > 0) {
            builder.append(String.format(Locale.getDefault(), "%1$02d", hours))
            builder.append(":")
        }
        builder.append(String.format(Locale.getDefault(), "%1$02d", minutes))
        builder.append(":")
        builder.append(String.format(Locale.getDefault(), "%1$02d", seconds))
        return builder.toString()
    }
}
