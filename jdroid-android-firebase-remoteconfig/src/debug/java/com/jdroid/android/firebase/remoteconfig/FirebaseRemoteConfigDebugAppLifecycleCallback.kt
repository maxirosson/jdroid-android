package com.jdroid.android.firebase.remoteconfig

import android.content.Context

import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.debug.DebugSettingsHelper
import com.jdroid.android.lifecycle.ApplicationLifecycleCallback

class FirebaseRemoteConfigDebugAppLifecycleCallback : ApplicationLifecycleCallback() {

    override fun onProviderInit(context: Context) {

        if (MockRemoteConfigLoader.isMocksEnabled()) {
            AbstractApplication.get().remoteConfigLoader = MockRemoteConfigLoader(true)
        }

        DebugSettingsHelper.addPreferencesAppender(FirebaseRemoteConfigPrefsAppender())
    }

    override fun getInitOrder(): Int {
        return 1
    }
}
