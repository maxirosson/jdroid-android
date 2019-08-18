package com.jdroid.android.about

import com.jdroid.android.context.AbstractAppContext
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.utils.AppUtils
import com.jdroid.java.utils.ReflectionUtils

open class AboutContext : AbstractAppContext() {

    @Suppress("UNCHECKED_CAST")
    fun getAboutFragmentClass(): Class<out AbstractFragment> {
        return ReflectionUtils.getClass("com.jdroid.android.about.AboutFragment") as Class<out AbstractFragment>
    }

    @Suppress("UNCHECKED_CAST")
    fun getLibrariesFragmentClass(): Class<out AbstractFragment> {
        return ReflectionUtils.getClass("com.jdroid.android.about.LibrariesFragment") as Class<out AbstractFragment>
    }

    open fun getSpreadTheLoveFragmentClass(): Class<out AbstractFragment>? {
        return null
    }

    open fun isBetaTestingEnabled(): Boolean {
        return false
    }

    open fun getBetaTestingUrl(): String {
        return "https://play.google.com/apps/testing/" + AppUtils.getReleaseApplicationId()
    }
}
