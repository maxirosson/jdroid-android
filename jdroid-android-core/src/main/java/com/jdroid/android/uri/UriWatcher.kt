package com.jdroid.android.uri

import android.net.Uri
import androidx.annotation.MainThread

interface UriWatcher {

    /**
     * Called when an Uri is opened on the App
     *
     *
     * This method shouldn't perform long operations, because could block the user interface.
     *
     * @param uri The opened Uri
     */
    @MainThread
    fun onUriOpened(uri: Uri)
}
