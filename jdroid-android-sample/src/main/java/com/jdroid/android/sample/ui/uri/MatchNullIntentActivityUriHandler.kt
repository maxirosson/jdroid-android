package com.jdroid.android.sample.ui.uri

import android.net.Uri

import com.jdroid.android.uri.AbstractUriHandler

class MatchNullIntentActivityUriHandler : AbstractUriHandler<MatchNullIntentActivity>() {

    override fun matches(uri: Uri): Boolean {
        return true
    }
}
