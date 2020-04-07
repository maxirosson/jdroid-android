package com.jdroid.android.firebase.analytics

import com.jdroid.android.koin.KoinHelper
import com.jdroid.java.analytics.AnalyticsTracker
import org.koin.core.get
import java.util.concurrent.Executor

open class AbstractFirebaseAnalyticsTracker : AnalyticsTracker {

    override fun isEnabled(): Boolean {
        return true
    }

    protected fun getFirebaseAnalyticsFacade(): FirebaseAnalyticsFacade {
        return KoinHelper.get()
    }

    override fun getExecutor(): Executor {
        return getFirebaseAnalyticsFacade().executor
    }
}
