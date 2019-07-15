package com.jdroid.android.share

import android.app.Activity
import android.graphics.drawable.Drawable

import com.jdroid.android.utils.ExternalAppsUtils

abstract class SharingItem {

    abstract fun getApplicationId(): String?

    open fun getMinimumVersionCode(): Int? {
        return null
    }

    open fun getAppIcon(): Drawable? {
        return ExternalAppsUtils.getAppIcon(getApplicationId()!!)
    }

    abstract fun share(activity: Activity)

    open fun isEnabled(): Boolean {
        val applicationId = getApplicationId()
        return applicationId != null && ExternalAppsUtils.isAppInstalled(applicationId, getMinimumVersionCode())
    }
}
