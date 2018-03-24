package com.jdroid.android.utils;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.IntDef;
import android.support.annotation.RequiresPermission;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.ValidationUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

public class AndroidUtils {
	
	public static Integer getApiLevel() {
		return android.os.Build.VERSION.SDK_INT;
	}
	
	public static Boolean isPreLollipop() {
		return android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;
	}

	public static String getPlatformVersion() {
		return android.os.Build.VERSION.RELEASE;
	}
	
	@RequiresPermission(Manifest.permission.GET_ACCOUNTS)
	public static List<String> getAccountsEmails() {
		List<String> emails = Lists.newArrayList();
		for (Account account : AccountManager.get(AbstractApplication.get()).getAccounts()) {
			if (ValidationUtils.isValidEmail(account.name) && !emails.contains(account.name)) {
				emails.add(account.name);
			}
		}
		return emails;
	}
	
	public static Boolean isMainThread() {
		return Looper.getMainLooper().getThread() == Thread.currentThread();
	}
	
	public static void setRequestedOrientation(Activity activity, @ScreenOrientation int requestedOrientation) {
		try {
			activity.setRequestedOrientation(requestedOrientation);
		} catch(IllegalStateException e) {
			// This is to catch a validation added on android 8.0 (and removed on android 8.1)
			if (!"Only fullscreen activities can request orientation".equals(e.getMessage())) {
				AbstractApplication.get().getExceptionHandler().logHandledException(e);
			} else {
				LoggerUtils.getLogger(activity.getClass()).warn(e.getMessage(), e);
			}
		}
	}
	
	@IntDef({
			ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED,
			ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE,
			ActivityInfo.SCREEN_ORIENTATION_PORTRAIT,
			ActivityInfo.SCREEN_ORIENTATION_USER,
			ActivityInfo.SCREEN_ORIENTATION_BEHIND,
			ActivityInfo.SCREEN_ORIENTATION_SENSOR,
			ActivityInfo.SCREEN_ORIENTATION_NOSENSOR,
			ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE,
			ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT,
			ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE,
			ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT,
			ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR,
			ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE,
			ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT,
			ActivityInfo.SCREEN_ORIENTATION_FULL_USER,
			ActivityInfo.SCREEN_ORIENTATION_LOCKED
	})
	@Retention(RetentionPolicy.SOURCE)
	public @interface ScreenOrientation {}

}
