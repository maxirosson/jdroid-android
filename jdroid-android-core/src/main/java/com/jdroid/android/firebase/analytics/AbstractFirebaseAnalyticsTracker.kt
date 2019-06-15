package com.jdroid.android.firebase.analytics

import androidx.annotation.WorkerThread
import com.jdroid.java.analytics.AnalyticsTracker
import java.util.concurrent.Executor

open class AbstractFirebaseAnalyticsTracker : AnalyticsTracker {

    override fun isEnabled(): Boolean {
        return true
    }

    @WorkerThread
    protected fun getFirebaseAnalyticsHelper(): FirebaseAnalyticsHelper {
        return FirebaseAnalyticsFactory.getFirebaseAnalyticsHelper()
    }

    override fun getExecutor(): Executor {
        return getFirebaseAnalyticsHelper().executor
    }
}
