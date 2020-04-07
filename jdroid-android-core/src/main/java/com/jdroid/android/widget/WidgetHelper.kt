package com.jdroid.android.widget

import android.app.PendingIntent
import android.content.Intent
import android.net.Uri

import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.concurrent.AppExecutors
import com.jdroid.android.uri.ReferrerUtils
import com.jdroid.android.uri.UriUtils
import com.jdroid.android.utils.AppUtils
import com.jdroid.android.utils.SharedPreferencesHelper
import com.jdroid.java.utils.RandomUtils
import com.jdroid.java.utils.StringUtils

object WidgetHelper {

    private const val WIDGET_PREFERENCES = "widgets"
    private const val WIDGET_NAMES = "widgetsNames"
    const val WIDGET_SCHEME = "widget"

    @Synchronized
    fun onWidgetRemoved(widgetName: String) {
        AppExecutors.diskIOExecutor.execute {
            val sharedPreferencesHelper = SharedPreferencesHelper.get(WIDGET_PREFERENCES)
            val widgets = sharedPreferencesHelper.loadPreferenceAsStringList(WIDGET_NAMES)
            if (widgets.contains(widgetName)) {
                widgets.remove(widgetName)
                sharedPreferencesHelper.savePreference(WIDGET_NAMES, widgets)
                AbstractApplication.get().coreAnalyticsSender.trackWidgetRemoved(widgetName)
            }
        }
    }

    @Synchronized
    fun onWidgetAdded(widgetName: String) {
        AppExecutors.diskIOExecutor.execute {
            val sharedPreferencesHelper = SharedPreferencesHelper.get(WIDGET_PREFERENCES)
            val widgets = sharedPreferencesHelper.loadPreferenceAsStringList(WIDGET_NAMES)
            if (!widgets.contains(widgetName)) {
                sharedPreferencesHelper.appendPreferenceAsync(WIDGET_NAMES, widgetName)
                AbstractApplication.get().coreAnalyticsSender.trackWidgetAdded(widgetName)
            }
        }
    }

    fun createPendingIntent(url: String): PendingIntent? {
        if (StringUtils.isNotEmpty(url)) {
            val widgetIntent = UriUtils.createIntent(AbstractApplication.get(), url, generateWidgetReferrer())
            return createPendingIntent(widgetIntent)
        } else {
            return null
        }
    }

    fun createPendingIntent(intent: Intent): PendingIntent {

        // This is a hack to avoid the notification caching
        if (intent.data == null) {
            intent.data = Uri.parse(WIDGET_SCHEME + "://" + RandomUtils.getInt())
        }

        ReferrerUtils.setReferrer(intent, generateWidgetReferrer())

        return PendingIntent.getActivity(AbstractApplication.get(), RandomUtils.get16BitsInt(), intent, 0)
    }

    fun generateWidgetReferrer(): String {
        return WIDGET_SCHEME + "://" + AppUtils.getApplicationId()
    }
}
