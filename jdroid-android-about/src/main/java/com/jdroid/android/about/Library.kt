package com.jdroid.android.about

import android.app.Activity

import com.jdroid.android.utils.ExternalAppsUtils

class Library(val libraryKey: String, val name: String, val author: String, val url: String) {

    fun onSelected(activity: Activity) {
        ExternalAppsUtils.openUrl(url)
    }
}
