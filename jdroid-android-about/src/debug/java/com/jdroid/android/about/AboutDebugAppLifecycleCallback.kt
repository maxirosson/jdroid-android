package com.jdroid.android.about

import android.content.Context

import com.jdroid.android.debug.DebugSettingsHelper
import com.jdroid.android.lifecycle.ApplicationLifecycleCallback

class AboutDebugAppLifecycleCallback : ApplicationLifecycleCallback() {

    override fun onProviderInit(context: Context) {
        DebugSettingsHelper.addPreferencesAppender(RateAppDebugPrefsAppender())
    }
}
