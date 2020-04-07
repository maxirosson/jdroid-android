package com.jdroid.android.sample.ui.uri

import android.app.Activity
import android.content.Intent
import android.net.Uri

import com.jdroid.android.uri.AbstractUriHandler

class MainIntentErrorUriHandler : AbstractUriHandler<MainIntentErrorActivity>() {
    override fun matches(uri: Uri): Boolean {
        return true
    }

    override fun createMainIntent(activity: Activity, uri: Uri): Intent? {
        throw RuntimeException()
    }
}
