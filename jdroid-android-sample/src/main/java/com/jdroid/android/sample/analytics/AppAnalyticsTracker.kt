package com.jdroid.android.sample.analytics

import com.jdroid.java.analytics.AnalyticsTracker

interface AppAnalyticsTracker : AnalyticsTracker {

    fun trackExampleEvent()

    fun trackExampleTransaction()

    fun trackExampleTiming()
}
