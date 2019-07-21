package com.jdroid.android.sample.debug

import android.content.Context

import com.jdroid.android.firebase.fcm.AbstractFcmDebugAppLifecycleCallback
import com.jdroid.android.firebase.fcm.FcmDebugPrefsAppender
import com.jdroid.android.sample.firebase.fcm.SampleFcmMessage

class FcmDebugAppLifecycleCallback : AbstractFcmDebugAppLifecycleCallback() {

    override fun onProviderInit(context: Context) {
        super.onProviderInit(context)
        FcmDebugPrefsAppender.addFcmMessage(SampleFcmMessage())
    }
}
