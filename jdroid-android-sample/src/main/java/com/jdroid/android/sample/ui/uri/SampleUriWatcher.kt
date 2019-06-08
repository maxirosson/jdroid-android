package com.jdroid.android.sample.ui.uri

import android.net.Uri
import com.jdroid.android.uri.UriWatcher
import com.jdroid.java.utils.LoggerUtils

class SampleUriWatcher : UriWatcher {

    companion object {
        private val LOGGER = LoggerUtils.getLogger(SampleUriWatcher::class.java)
    }

    override fun onUriOpened(uri: Uri) {
        LOGGER.debug("SampleUriWatcher executed for Uri $uri")
    }
}
