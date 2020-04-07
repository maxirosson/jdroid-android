package com.jdroid.android.view

import com.jdroid.android.AbstractUnitTest

import junit.framework.Assert

import org.junit.Test

import java.util.concurrent.TimeUnit

class DefaultTimerViewFormatterTest : AbstractUnitTest() {

    @Test
    fun formatDurationTest() {
        val timerViewFormatter = DefaultTimerViewFormatter()
        Assert.assertEquals("00:00", timerViewFormatter.formatDuration(0))
        Assert.assertEquals("00:01", timerViewFormatter.formatDuration(TimeUnit.SECONDS.toMillis(1)))
        Assert.assertEquals("00:59", timerViewFormatter.formatDuration(TimeUnit.SECONDS.toMillis(59)))
        Assert.assertEquals("01:00", timerViewFormatter.formatDuration(TimeUnit.MINUTES.toMillis(1)))
        Assert.assertEquals(
            "59:59",
            timerViewFormatter.formatDuration(TimeUnit.MINUTES.toMillis(59) + TimeUnit.SECONDS.toMillis(59))
        )
        Assert.assertEquals("01:00:00", timerViewFormatter.formatDuration(TimeUnit.HOURS.toMillis(1)))
        Assert.assertEquals(
            "01:59:59",
            timerViewFormatter.formatDuration(
                TimeUnit.HOURS.toMillis(1) + TimeUnit.MINUTES.toMillis(59) + TimeUnit.SECONDS.toMillis(59)
            )
        )
        Assert.assertEquals(
            "99:59:59",
            timerViewFormatter.formatDuration(
                TimeUnit.HOURS.toMillis(99) + TimeUnit.MINUTES.toMillis(59) + TimeUnit.SECONDS.toMillis(59)
            )
        )
    }
}
