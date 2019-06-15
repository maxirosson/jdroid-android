package com.jdroid.android.google.inappbilling

import android.content.Context

import com.jdroid.android.debug.DebugSettingsHelper
import com.jdroid.android.lifecycle.ApplicationLifecycleCallback

class InAppBillingDebugAppLifecycleCallback : ApplicationLifecycleCallback() {

    override fun onProviderInit(context: Context) {
        DebugSettingsHelper.addPreferencesAppender(InAppBillingDebugPrefsAppender())
    }
}
