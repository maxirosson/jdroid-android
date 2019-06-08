package com.jdroid.android.uri

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.google.firebase.appindexing.Action
import com.jdroid.android.application.AbstractApplication
import com.jdroid.java.utils.LoggerUtils

abstract class AbstractUriHandler<T : Activity> : UriHandler<T> {

    companion object {
        private val LOGGER = LoggerUtils.getLogger(AbstractUriHandler::class.java)
    }

    override fun matches(uri: Uri): Boolean {
        return true
    }

    override fun logUriNotMatch(uri: Uri) {
        AbstractApplication.get().exceptionHandler.logWarningException(javaClass.simpleName + " matches the default intent: " + uri.toString())
    }

    override fun createMainIntent(activity: Activity, uri: Uri): Intent? {
        return null
    }

    override fun createDefaultIntent(activity: Activity, uri: Uri): Intent? {
        return Intent(activity, AbstractApplication.get().homeActivityClass)
    }

    override fun getUrl(activity: T): String? {
        return null
    }

    override fun isAppIndexingEnabled(activity: T): Boolean {
        return true
    }

    override fun getAppIndexingAction(activity: T): Action? {
        if (isAppIndexingEnabled(activity)) {
            val url = getUrl(activity)
            if (url != null) {
                val actionType = Action.Builder.VIEW_ACTION
                val title = getAppIndexingTitle(activity)
                LOGGER.debug("New App Indexing Action created. Type: $actionType | Title: $title | Url: $url")
                return Action.Builder(actionType).setObject(title, url).build()
            }
        }
        return null
    }

    protected fun getAppIndexingTitle(activity: T): String {
        return activity.title.toString()
    }
}
