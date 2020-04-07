package com.jdroid.android.shortcuts

import android.content.Context

import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.lifecycle.ApplicationLifecycleCallback

class AppShortcutsAppLifecycleCallback : ApplicationLifecycleCallback() {

    override fun onProviderInit(context: Context) {
        AbstractApplication.get().addCoreAnalyticsTracker(AppShortcutsTracker())
    }

    override fun onLocaleChanged(context: Context) {
        AppShortcutsHelper.registerDynamicShortcuts()
    }

    override fun isEnabled(): Boolean {
        return AppShortcutsHelper.isDynamicAppShortcutsSupported()
    }
}
