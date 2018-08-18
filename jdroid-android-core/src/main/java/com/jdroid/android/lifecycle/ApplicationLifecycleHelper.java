package com.jdroid.android.lifecycle;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.support.annotation.MainThread;

import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApplicationLifecycleHelper {

	private static final Logger LOGGER = LoggerUtils.getLogger(ApplicationLifecycleHelper.class);

	private static final String METADATA_VALUE = "ApplicationLifecycleCallback";

	private static List<ApplicationLifecycleCallback> applicationLifecycleCallbacks;

	@MainThread
	public static void onProviderInit(Context context) {

		LOGGER.debug("Executing init on AbstractInitProvider");

		init(context);
		for (ApplicationLifecycleCallback callback : applicationLifecycleCallbacks) {
			if (callback.isEnabled()) {
				LOGGER.debug("Executing onProviderInit for " + callback.getClass().getName());
				callback.onProviderInit(context);
			}
		}
	}

	@MainThread
	public static void attachBaseContext(Context context) {

		AppContextContainer.setApplicationContext(context);

		LOGGER.debug("Executing attachBaseContext on application");

		init(context);
		for (ApplicationLifecycleCallback callback : applicationLifecycleCallbacks) {
			if (callback.isEnabled()) {
				LOGGER.debug("Executing attachBaseContext for " + callback.getClass().getName());
				callback.attachBaseContext(context);
			}
		}
	}

	@MainThread
	public static void onCreate(Context context) {

		LOGGER.debug("Executing onCreate on application");

		init(context);
		for (ApplicationLifecycleCallback callback : applicationLifecycleCallbacks) {
			if (callback.isEnabled()) {
				LOGGER.debug("Executing onCreate for " + callback.getClass().getName());
				callback.onCreate(context);
			}
		}
	}

	@MainThread
	public static void onConfigurationChanged(Context context, Configuration newConfig) {

		LOGGER.debug("Executing onConfigurationChanged on application");

		init(context);
		for (ApplicationLifecycleCallback callback : applicationLifecycleCallbacks) {
			if (callback.isEnabled()) {
				LOGGER.debug("Executing onConfigurationChanged for " + callback.getClass().getName());
				callback.onConfigurationChanged(context, newConfig);
			}
		}
	}

	@MainThread
	public static void onLowMemory(Context context) {

		LOGGER.debug("Executing onLowMemory on application");

		init(context);
		for (ApplicationLifecycleCallback callback : applicationLifecycleCallbacks) {
			if (callback.isEnabled()) {
				LOGGER.debug("Executing onLowMemory for " + callback.getClass().getName());
				callback.onLowMemory(context);
			}
		}
	}

	@MainThread
	public static void onLocaleChanged(Context context) {

		LOGGER.trace("Executing onLocaleChanged on application");

		init(context);
		for (ApplicationLifecycleCallback callback : applicationLifecycleCallbacks) {
			if (callback.isEnabled()) {
				LOGGER.debug("Executing onLocaleChanged for " + callback.getClass().getName());
				callback.onLocaleChanged(context);
			}
		}
	}

	private static void init(Context context) {
		if (applicationLifecycleCallbacks == null) {
			applicationLifecycleCallbacks = ApplicationLifecycleHelper.parseApplicationLifecycleCallbacks(context);
		}
	}

	private static List<ApplicationLifecycleCallback> parseApplicationLifecycleCallbacks(Context context) {

		LOGGER.debug("Loading application lifecycle callbacks");

		List<ApplicationLifecycleCallback> applicationLifecycleCallbacks = new ArrayList<>();
		try {
			ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			if (appInfo.metaData == null) {
				LOGGER.debug("Got null app info metadata");
				return applicationLifecycleCallbacks;
			}

			for (String key : appInfo.metaData.keySet()) {
				if (METADATA_VALUE.equals(appInfo.metaData.get(key))) {
					applicationLifecycleCallbacks.add(parseApplicationLifecycleCallback(key));
					LOGGER.debug("Loaded ApplicationLifecycleCallback: " + key);
				}
			}

		} catch (PackageManager.NameNotFoundException e) {
			throw new RuntimeException("Unable to find metadata to parse ApplicationLifecycleCallback", e);
		}

		LOGGER.debug("Finished loading application lifecycle callbacks");

		Collections.sort(applicationLifecycleCallbacks);

		return applicationLifecycleCallbacks;
	}

	@SuppressWarnings("TryWithIdenticalCatches")
	private static ApplicationLifecycleCallback parseApplicationLifecycleCallback(String className) {
		Class<?> clazz;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(className + " is not a ApplicationLifecycleCallback implementation", e);
		}

		Object callback;
		try {
			callback = clazz.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException("Unable to instantiate ApplicationLifecycleCallback implementation for " + clazz, e);
			// These can't be combined until API minimum is 19.
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Unable to instantiate ApplicationLifecycleCallback implementation for " + clazz, e);
		}

		if (!(callback instanceof ApplicationLifecycleCallback)) {
			throw new RuntimeException("Expected instanceof ApplicationLifecycleCallback, but found: " + callback);
		}
		return (ApplicationLifecycleCallback)callback;
	}
}
