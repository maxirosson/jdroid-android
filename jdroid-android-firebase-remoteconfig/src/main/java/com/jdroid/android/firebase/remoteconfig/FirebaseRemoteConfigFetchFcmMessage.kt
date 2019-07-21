package com.jdroid.android.firebase.remoteconfig

import com.google.firebase.messaging.RemoteMessage
import com.jdroid.android.firebase.fcm.AbstractFcmMessage
import com.jdroid.android.utils.SharedPreferencesHelper

class FirebaseRemoteConfigFetchFcmMessage : AbstractFcmMessage() {

    override fun getMessageKey(): String {
        return "firebaseRemoteConfigFetchFcmMessage"
    }

    override fun handle(remoteMessage: RemoteMessage) {
        SharedPreferencesHelper.get().savePreferenceAsync(FirebaseRemoteConfigLoader.CONFIG_STALE, true)
        FirebaseRemoteConfigFetchCommand().start()
    }
}
