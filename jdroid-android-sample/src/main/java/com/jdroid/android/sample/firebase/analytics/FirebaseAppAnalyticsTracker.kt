package com.jdroid.android.sample.firebase.analytics

import com.jdroid.android.firebase.analytics.AbstractFirebaseAnalyticsTracker
import com.jdroid.android.firebase.analytics.FirebaseAnalyticsParams
import com.jdroid.android.sample.analytics.AppAnalyticsTracker

class FirebaseAppAnalyticsTracker : AbstractFirebaseAnalyticsTracker(), AppAnalyticsTracker {

    override fun trackExampleEvent() {
        val params = FirebaseAnalyticsParams()
        params.put("sample_key_1", "sample_value")
        params.put("sample_key_2", true)
        params.put("sample_key_3", 1L)
        params.put("sample_key_with_too_long_name_will_be_truncated", 1L)
        params.put("sample_key_with_too_long_value", "sample_value_with_too_long_name_will_be_truncated_and_log_a_warning_123456789012345678901234567890123456789")
        getFirebaseAnalyticsFacade().sendEvent("sample_event_1", params)
        getFirebaseAnalyticsFacade().sendEvent("sample_event_2")
        getFirebaseAnalyticsFacade().sendEvent("sample_event_with_too_long_name")
    }

    override fun trackExampleTransaction() {
        // Do nothing
    }

    override fun trackExampleTiming() {
        // Do nothing
    }
}
