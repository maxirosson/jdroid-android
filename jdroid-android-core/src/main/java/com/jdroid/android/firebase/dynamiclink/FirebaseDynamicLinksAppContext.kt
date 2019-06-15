package com.jdroid.android.firebase.dynamiclink

import com.jdroid.android.context.BuildConfigUtils

object FirebaseDynamicLinksAppContext {

    fun isFirebaseDynamicLinksEnabled(): Boolean {
        return BuildConfigUtils.getBuildConfigBoolean("FIREBASE_DYNAMIC_LINKS_ENABLED", true)
    }

    fun getDynamicLinksDomain(): String? {
        return BuildConfigUtils.getBuildConfigValue("FIREBASE_DYNAMIC_LINKS_DOMAIN")
    }

    fun getWebApiKey(): String? {
        return BuildConfigUtils.getBuildConfigValue("FIREBASE_WEB_API_KEY")
    }
}
