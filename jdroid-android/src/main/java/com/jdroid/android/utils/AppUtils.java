package com.jdroid.android.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;

import com.jdroid.android.application.AbstractApplication;

public class AppUtils {

	private AppUtils(){}

	/**
	 * @return The version name of the application
	 */
	public static String getVersionName() {
		return getPackageInfo().versionName;
	}

	/**
	 * @return The version code of the application
	 */
	public static Integer getVersionCode() {
		return getPackageInfo().versionCode;
	}

	/**
	 * @return The application id of the application
	 */
	public static String getApplicationId() {
		return getPackageInfo().packageName;
	}

	/**
	 * @return The name of the application
	 */
	public static String getApplicationName() {
		Context context = AbstractApplication.get();
		return context.getPackageManager().getApplicationLabel(getApplicationInfo()).toString();
	}

	public static PackageInfo getPackageInfo() {
		PackageInfo info = null;
		try {
			Context context = AbstractApplication.get();
			info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			// Do Nothing
		}
		return info;
	}

	public static ApplicationInfo getApplicationInfo() {
		ApplicationInfo info = null;
		try {
			Context context = AbstractApplication.get();
			info = context.getPackageManager().getApplicationInfo(context.getPackageName(),
					PackageManager.GET_META_DATA);
		} catch (PackageManager.NameNotFoundException e) {
			// Do Nothing
		}
		return info;
	}

	public static void showSoftInput(Activity activity) {
		activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
	}

	public static void hideSoftInput(View view) {
		((InputMethodManager)AbstractApplication.get().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
				view.getWindowToken(), 0);
	}

	public static void scrollToBottom(final ScrollView scroll) {
		if (scroll != null) {
			scroll.post(new Runnable() {

				@Override
				public void run() {
					scroll.fullScroll(View.FOCUS_DOWN);
				}
			});
		}
	}

	/**
	 * Checks if the application is installed on the SD card.
	 *
	 * @return <code>true</code> if the application is installed on the sd card
	 */
	public static Boolean isInstalledOnSdCard() {
		return (getPackageInfo().applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == ApplicationInfo.FLAG_EXTERNAL_STORAGE;
	}
}
