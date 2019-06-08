package com.jdroid.android.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import com.jdroid.java.utils.LoggerUtils

abstract class AbstractWidgetProvider : AppWidgetProvider() {

    protected abstract val widgetName: String

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)

        WidgetHelper.onWidgetRemoved(widgetName)
        LOGGER.info("App widgets deleted: $appWidgetIds")
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)

        LOGGER.info("App widget enabled")
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)

        LOGGER.info("App widget disabled")
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        WidgetHelper.onWidgetAdded(widgetName)
        LOGGER.info("App widgets updated: $appWidgetIds")
    }

    companion object {

        private val LOGGER = LoggerUtils.getLogger(AbstractWidgetProvider::class.java)
    }
}
