package com.jdroid.android.date

import com.jdroid.android.application.AbstractApplication
import com.jdroid.java.date.DateUtils
import java.util.Date

/**
 * Date Utils that returns formatted times & dates according to the current locale and user preferences
 */
object AndroidDateUtils {

    /**
     * @return The formatted time
     */
    fun formatTime(): String? {
        return formatTime(DateUtils.now())
    }

    /**
     * @param date The [Date] to format
     * @return The formatted time
     */
    fun formatTime(date: Date?): String? {
        val timeFormat = android.text.format.DateFormat.getTimeFormat(AbstractApplication.get())
        return DateUtils.format(date, timeFormat)
    }

    /**
     * @return The formatted date
     */
    fun formatDate(): String? {
        return formatDate(DateUtils.now())
    }

    /**
     * @param date The [Date] to format
     * @return The formatted date
     */
    fun formatDate(date: Date?): String? {
        val dateFormat = android.text.format.DateFormat.getDateFormat(AbstractApplication.get())
        return DateUtils.format(date, dateFormat)
    }
}
