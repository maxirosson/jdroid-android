package com.jdroid.android.glide.debug

import android.content.Context

import com.jdroid.android.debug.DebugSettingsHelper
import com.jdroid.android.lifecycle.ApplicationLifecycleCallback

class GlideDebugAppLifecycleCallback : ApplicationLifecycleCallback() {

    override fun onProviderInit(context: Context) {
        super.onProviderInit(context)

        DebugSettingsHelper.addPreferencesAppender(GlideDebugPrefsAppender())
    }
}
