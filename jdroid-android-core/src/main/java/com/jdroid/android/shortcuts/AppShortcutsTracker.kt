package com.jdroid.android.shortcuts

import android.app.Activity

import com.jdroid.android.analytics.AbstractCoreAnalyticsTracker

class AppShortcutsTracker : AbstractCoreAnalyticsTracker() {

    override fun onFirstActivityCreate(activity: Activity) {
        AppShortcutsHelper.registerDynamicShortcuts()
    }
}
