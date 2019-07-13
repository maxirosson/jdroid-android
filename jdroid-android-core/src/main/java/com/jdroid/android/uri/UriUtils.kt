package com.jdroid.android.uri

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.intent.IntentUtils
import com.jdroid.android.notification.NotificationBuilder.NOTIFICATION_SCHEME
import com.jdroid.android.utils.AppUtils
import com.jdroid.android.widget.WidgetHelper
import com.jdroid.java.utils.RandomUtils
import com.jdroid.java.utils.StringUtils

object UriUtils {

    private const val ORIGINAL_URI = "originalUri"
    private const val RANDOM_PARAMETER = "rnd"
    private const val INTERNAL_SCHEME = "internal"

    fun createInternalIntent(context: Context, url: String): Intent {
        return createIntent(context, url, INTERNAL_SCHEME + "://" + AppUtils.getApplicationId())
    }

    fun createIntent(context: Context, url: String?, referrer: String?): Intent {
        var intent: Intent
        if (url != null) {
            intent = Intent()
            intent.putExtra(ORIGINAL_URI, url)
            intent.action = Intent.ACTION_VIEW
            intent.data = addRandomParam(Uri.parse(url))
            intent.setPackage(AppUtils.getApplicationId())
            if (!IntentUtils.isIntentAvailable(intent)) {
                intent = Intent(context, AbstractApplication.get().homeActivityClass)
                AbstractApplication.get().exceptionHandler.logHandledException("Url is not valid: $url")
            }
        } else {
            intent = Intent(context, AbstractApplication.get().homeActivityClass)
            AbstractApplication.get().exceptionHandler.logHandledException("Missing url to create intent")
        }
        ReferrerUtils.setReferrer(intent, referrer)
        return intent
    }

    fun addRandomParam(uri: Uri): Uri {
        val uriString = uri.toString()
        val builder = StringBuilder(uriString)
        if (uri.pathSegments.isEmpty() && StringUtils.isEmpty(uri.query) && !uriString.endsWith("/")) {
            builder.append("/")
        }
        if (StringUtils.isEmpty(uri.query)) {
            builder.append("?")
        } else {
            builder.append("&")
        }
        builder.append(RANDOM_PARAMETER)
        builder.append("=")
        builder.append(RandomUtils.getInt())
        return Uri.parse(builder.toString())
    }

    fun getUri(intent: Intent): Uri? {
        var uri = intent.data
        if (uri != null) {
            val originalUri = intent.getStringExtra(ORIGINAL_URI)
            if (originalUri != null) {
                uri = Uri.parse(originalUri)
            }
        }
        return if (uri != null && (uri.scheme == NOTIFICATION_SCHEME || uri.scheme == WidgetHelper.WIDGET_SCHEME)) null else uri
    }

    fun getUri(activity: Activity): Uri? {
        return getUri(activity.intent)
    }

    fun isInternalReferrerCategory(referrerCategory: String): Boolean {
        return referrerCategory.startsWith("$INTERNAL_SCHEME://")
    }
}
