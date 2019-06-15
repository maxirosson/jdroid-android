package com.jdroid.android.shortcuts.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.jdroid.android.firebase.analytics.AbstractFirebaseAnalyticsTracker
import com.jdroid.android.firebase.analytics.FirebaseAnalyticsParams

class FirebaseAppShortcutsAnalyticsTracker : AbstractFirebaseAnalyticsTracker(), AppShortcutsAnalyticsTracker {

    override fun trackPinShortcut(shortcutName: String) {
        val params = FirebaseAnalyticsParams()
        params.put(FirebaseAnalytics.Param.ITEM_ID, shortcutName)
        getFirebaseAnalyticsHelper().sendEvent("pin_shortcut", params)
    }
}
