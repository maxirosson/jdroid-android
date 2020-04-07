package com.jdroid.android.sample.ui.uri

import android.app.Activity
import android.content.Intent
import android.net.Uri

import com.jdroid.android.sample.ui.home.HomeActivity
import com.jdroid.android.uri.AbstractUriHandler

class UriMapperNoFlagsUriHandler : AbstractUriHandler<UriMapperNoFlagsActivity>() {

    override fun matches(uri: Uri): Boolean {
        return uri.getQueryParameter("a") == "1" || uri.getQueryParameter("a") == "2"
    }

    override fun createDefaultIntent(activity: Activity, uri: Uri): Intent? {
        return Intent(activity, HomeActivity::class.java)
    }

    override fun getUrl(activity: UriMapperNoFlagsActivity): String? {
        return "http://jdroidtools.com/uri/noflags?a=1"
    }
}
