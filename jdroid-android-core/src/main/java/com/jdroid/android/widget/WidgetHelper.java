package com.jdroid.android.widget;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.uri.ReferrerUtils;
import com.jdroid.android.uri.UriUtils;
import com.jdroid.android.utils.AppUtils;
import com.jdroid.android.utils.SharedPreferencesHelper;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.utils.RandomUtils;
import com.jdroid.java.utils.StringUtils;

import java.util.List;

public class WidgetHelper {

	private static final String WIDGET_PREFERENCES = "widgets";
	private static final String WIDGET_NAMES = "widgetsNames";
	public static final String WIDGET_SCHEME = "widget";

	public synchronized static void onWidgetRemoved(final String widgetName) {
		ExecutorUtils.execute(new Runnable() {
			@Override
			public void run() {
				SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.get(WIDGET_PREFERENCES);
				List<String> widgets = sharedPreferencesHelper.loadPreferenceAsStringList(WIDGET_NAMES);
				if (widgets.contains(widgetName)) {
					widgets.remove(widgetName);
					sharedPreferencesHelper.savePreference(WIDGET_NAMES, widgets);
					AbstractApplication.get().getCoreAnalyticsSender().trackWidgetRemoved(widgetName);
				}
			}
		});
	}

	public synchronized static void onWidgetAdded(final String widgetName) {

		ExecutorUtils.execute(new Runnable() {
			@Override
			public void run() {
				SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.get(WIDGET_PREFERENCES);
				List<String> widgets = sharedPreferencesHelper.loadPreferenceAsStringList(WIDGET_NAMES);
				if (!widgets.contains(widgetName)) {
					sharedPreferencesHelper.appendPreferenceAsync(WIDGET_NAMES, widgetName);
					AbstractApplication.get().getCoreAnalyticsSender().trackWidgetAdded(widgetName);
				}
			}
		});
	}

	public static PendingIntent createPendingIntent(String url) {
		if (StringUtils.isNotEmpty(url)) {
			Intent widgetIntent = UriUtils.createIntent(AbstractApplication.get(), url, generateWidgetReferrer());
			return createPendingIntent(widgetIntent);
		} else {
			return null;
		}
	}

	public static PendingIntent createPendingIntent(Intent intent) {

		// This is a hack to avoid the notification caching
		if (intent.getData() == null) {
			intent.setData( Uri.parse(WIDGET_SCHEME + "://" + RandomUtils.getInt()));
		}

		ReferrerUtils.setReferrer(intent, generateWidgetReferrer());

		return PendingIntent.getActivity(AbstractApplication.get(), RandomUtils.get16BitsInt(), intent, 0);
	}

	public static String generateWidgetReferrer() {
		return WIDGET_SCHEME + "://" + AppUtils.getApplicationId();
	}
}
