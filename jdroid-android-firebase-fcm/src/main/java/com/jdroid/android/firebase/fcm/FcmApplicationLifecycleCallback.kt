package com.jdroid.android.firebase.fcm

import android.content.Context

import com.jdroid.android.lifecycle.ApplicationLifecycleCallback

class FcmApplicationLifecycleCallback : ApplicationLifecycleCallback() {

    override fun onLocaleChanged(context: Context) {
        AbstractFcmAppModule.get().startFcmRegistration(false)
    }
}
