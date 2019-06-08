package com.jdroid.android.sample.ui.uri

import android.net.Uri

import com.jdroid.android.uri.AbstractUriHandler

class NoMatchNewActivityUriHandler : AbstractUriHandler<NoMatchNewActivity>() {

    override fun matches(uri: Uri): Boolean {
        return false
    }
}
