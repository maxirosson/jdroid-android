package com.jdroid.android.sample.application

import com.jdroid.android.context.AppContext
import com.jdroid.android.sample.BuildConfig
import com.jdroid.android.sample.api.ApiServer
import com.jdroid.android.sample.firebase.remoteconfig.AndroidRemoteConfigParameter
import com.jdroid.java.http.Server
import com.jdroid.java.remoteconfig.RemoteConfigParameter

class AndroidAppContext : AppContext() {

    companion object {
        const val SAMPLE_BANNER_AD_UNIT_ID = "ca-app-pub-4654922738884963/2999432948"
        const val SAMPLE_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-4654922738884963/4476166148"
    }

    override fun findServerByName(name: String): Server? {
        return ApiServer.valueOf(name)
    }

    override fun getServerApiVersion(): String? {
        return "1.0"
    }

    override fun getWebsite(): String? {
        return "http://www.jdroidtools.com"
    }

    override fun getContactUsEmail(): String? {
        return "contact@jdroidtools.com"
    }

    override fun getTwitterAccount(): String? {
        return "jdroidtools"
    }

    override fun getPrivacyPolicyUrl(): RemoteConfigParameter? {
        return AndroidRemoteConfigParameter.PRIVACY_POLICY_URL
    }

    fun getFirebaseAuthToken(): String {
        return BuildConfig.FIREBASE_AUTH_TOKEN
    }
}
