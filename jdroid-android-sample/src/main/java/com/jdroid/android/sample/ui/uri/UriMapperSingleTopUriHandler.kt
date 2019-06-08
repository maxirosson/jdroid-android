package com.jdroid.android.sample.ui.uri

import android.app.Activity
import android.content.Intent
import android.net.Uri

import com.jdroid.android.uri.AbstractUriHandler

class UriMapperSingleTopUriHandler : AbstractUriHandler<UriMapperSingleTopActivity>() {

    override fun matches(uri: Uri): Boolean {
        return uri.getQueryParameter("a") == "1" || uri.getQueryParameter("a") == "2"
    }

    override fun createMainIntent(activity: Activity, uri: Uri): Intent? {
        return Intent(activity, UriMapperSingleTopActivity::class.java)
    }

    override fun getUrl(activity: UriMapperSingleTopActivity): String? {
        return "http://jdroidtools.com/uri/singletop?a=1"
    }
}
