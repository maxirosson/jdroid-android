package com.jdroid.android.sample.ui.uri

import com.jdroid.android.uri.AbstractUriHandler

class UriMapperUriHandler : AbstractUriHandler<UriMapperActivity>() {

    override fun getUrl(activity: UriMapperActivity): String? {
        return "http://jdroidtools.com/uri"
    }
}
