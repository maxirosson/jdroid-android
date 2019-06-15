package com.jdroid.android.firebase.analytics

import com.jdroid.android.koin.KoinHelper
import org.koin.core.get

@Deprecated("Remove this class when all the invokers are migrated to kotlin")
object FirebaseAnalyticsFactory {

    fun getFirebaseAnalyticsFacade(): FirebaseAnalyticsFacade {
        return KoinHelper.get()
    }
}
