package com.jdroid.android.sample.ui.home

import com.jdroid.android.uri.AbstractUriHandler

class HomeUriHandler : AbstractUriHandler<HomeActivity>() {

    override fun getUrl(activity: HomeActivity): String? {
        return "http://jdroidtools.com/"
    }

    override fun isAppIndexingEnabled(activity: HomeActivity): Boolean {
        return false
    }
}
