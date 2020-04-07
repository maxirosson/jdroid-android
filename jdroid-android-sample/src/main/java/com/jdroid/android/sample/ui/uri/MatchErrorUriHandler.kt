package com.jdroid.android.sample.ui.uri

import android.net.Uri

import com.jdroid.android.uri.AbstractUriHandler

class MatchErrorUriHandler : AbstractUriHandler<MatchErrorActivity>() {

    override fun matches(uri: Uri): Boolean {
        throw RuntimeException()
    }
}
