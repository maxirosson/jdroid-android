package com.jdroid.android.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;

public class MultiWindowUtils {

	@SuppressLint("NewApi")
	public static Boolean isInMultiWindowMode(Activity activity) {
		return AndroidUtils.getApiLevel() >= Build.VERSION_CODES.N && activity.isInMultiWindowMode();
	}

	@SuppressLint("NewApi")
	public static Boolean isInPictureInPictureMode(Activity activity) {
		return AndroidUtils.getApiLevel() >= Build.VERSION_CODES.N && activity.isInPictureInPictureMode();
	}
}
