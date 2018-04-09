package com.jdroid.android.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

public abstract class AbstractWidgetProvider extends AppWidgetProvider {

	private static final Logger LOGGER = LoggerUtils.getLogger(AbstractWidgetProvider.class);

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);

		WidgetHelper.onWidgetRemoved(getWidgetName());
		LOGGER.info("App widgets deleted: " + appWidgetIds);
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);

		LOGGER.info("App widget enabled");
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);

		LOGGER.info("App widget disabled");
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		WidgetHelper.onWidgetAdded(getWidgetName());
		LOGGER.info("App widgets updated: " + appWidgetIds);
	}

	protected abstract String getWidgetName();
}
