package com.jdroid.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import androidx.annotation.Nullable;

/**
 * Launcher for all the activities of the application
 */
public class ActivityLauncher {

	private static final Logger LOGGER = LoggerUtils.getLogger(ActivityLauncher.class);

	/**
	 * Launches the {@link AbstractApplication#getHomeActivityClass()}
	 */
	public static void startHomeActivity(@Nullable Activity activity) {
		if (activity != null) {
			if (activity.getClass() != AbstractApplication.get().getHomeActivityClass()) {
				Intent intent = new Intent(activity, AbstractApplication.get().getHomeActivityClass());
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				activity.startActivity(intent);
			}
		} else {
			LOGGER.warn("Null activity. Ignoring launch of " + AbstractApplication.get().getHomeActivityClass().getSimpleName());
		}
	}

	/**
	 * Launches a new {@link Activity}
	 *
	 * @param targetActivityClass The target {@link Activity} class to launch
	 * @param bundle The extra {@link Bundle}
	 */
	public static void startActivity(@Nullable Activity activity, Class<? extends Activity> targetActivityClass, Bundle bundle) {
		if (activity != null) {
			if (activity.getClass() != targetActivityClass) {
				Intent intent = new Intent(activity, targetActivityClass);
				if (bundle != null) {
					intent.putExtras(bundle);
				}
				activity.startActivity(intent);
			}
		} else {
			LOGGER.warn("Null activity. Ignoring launch of " + targetActivityClass.getSimpleName());
		}
	}

	/**
	 * Launches a new {@link Activity}
	 *
	 * @param targetActivityClass The target {@link Activity} class to launch
	 * @param bundle The extra {@link Bundle}
	 * @param requestCode If >= 0, this code will be returned in onActivityResult() when the activity exits.
	 */
	public static void startActivity(@Nullable Activity activity, Class<? extends Activity> targetActivityClass, Bundle bundle, int requestCode) {
		if (activity != null) {
			if (activity.getClass() != targetActivityClass) {
				Intent intent = new Intent(activity, targetActivityClass);
				intent.putExtras(bundle);
				activity.startActivityForResult(intent, requestCode);
			}
		} else {
			LOGGER.warn("Null activity. Ignoring launch of " + targetActivityClass.getSimpleName());
		}
	}

	public static void startActivityNewTask(Intent intent) {
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		AbstractApplication.get().startActivity(intent);
	}

	/**
	 * Launches a new {@link Activity}
	 *
	 * @param targetActivityClass The target {@link Activity} class to launch
	 */
	public static void startActivity(@Nullable Activity activity, Class<? extends Activity> targetActivityClass) {
		if (activity != null) {
			if (activity.getClass() != targetActivityClass) {
				Intent intent = new Intent(activity, targetActivityClass);
				activity.startActivity(intent);
			}
		} else {
			LOGGER.warn("Null activity. Ignoring launch of " + targetActivityClass.getSimpleName());
		}
	}

	public static void startActivity(@Nullable Activity activity, Intent intent) {
		if (activity != null) {
			activity.startActivity(intent);
		} else {
			LOGGER.warn("Null activity. Ignoring starting activity for intent: " + intent);
		}
	}

	public static void startActivityForResult(@Nullable Activity activity, Intent intent, int requestCode) {
		if (activity != null) {
			activity.startActivityForResult(intent, requestCode);
		} else {
			LOGGER.warn("Null activity. Ignoring starting activity for intent: " + intent);
		}
	}
}
