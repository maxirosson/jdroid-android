package com.jdroid.android.sample.firebase.fcm

import com.jdroid.android.firebase.fcm.AbstractFcmAppModule
import com.jdroid.android.firebase.fcm.FcmSender
import com.jdroid.android.sample.application.AndroidApplication

class AndroidFcmAppModule : AbstractFcmAppModule() {

    override fun getFcmSenders(): List<FcmSender> {
        return listOf(AndroidApplication.get().appContext.server as FcmSender)
    }
}
