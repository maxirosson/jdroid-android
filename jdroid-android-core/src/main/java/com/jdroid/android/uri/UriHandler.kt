package com.jdroid.android.uri

import android.app.Activity
import android.content.Intent
import android.net.Uri

import com.google.firebase.appindexing.Action

/**
 * Handler which parse the parameters and create an intent based on an uri
 */
interface UriHandler<T : Activity> {

    /**
     * @return Whether the uri should be transformed to a main or a default intent
     */
    fun matches(uri: Uri): Boolean

    /**
     * Create the intent for the proper activity based on the uri.
     *
     * @param activity the [Activity].
     * @param uri the uri to handle.
     * @return the Intent.
     */
    fun createMainIntent(activity: Activity, uri: Uri): Intent?

    fun createDefaultIntent(activity: Activity, uri: Uri): Intent?

    fun logUriNotMatch(uri: Uri)

    fun isAppIndexingEnabled(activity: T): Boolean

    fun getAppIndexingAction(activity: T): Action?

    fun getUrl(activity: T): String?
}
