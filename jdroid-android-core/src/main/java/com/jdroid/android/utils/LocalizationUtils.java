package com.jdroid.android.utils;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.exception.ErrorCode;

public final class LocalizationUtils {
	
	/**
	 * Returns a formatted string, using the localized resource as format and the supplied arguments
	 * 
	 * @param resId The resource id to obtain the format
	 * @param args arguments to replace format specifiers
	 * @return The localized and formatted string
	 */
	public static String getString(int resId, Object... args) {
		return AbstractApplication.get().getString(resId, args);
	}
	
	public static String getTitle(ErrorCode errorCode) {
		if ((errorCode != null) && (errorCode.getTitleResId() != null)) {
			return LocalizationUtils.getString(errorCode.getTitleResId());
		} else {
			return null;
		}
	}
	
	public static String getTitle(ErrorCode errorCode, Object... args) {
		if ((errorCode != null) && (errorCode.getTitleResId() != null)) {
			return LocalizationUtils.getString(errorCode.getTitleResId(), args);
		} else {
			return null;
		}
	}
	
	public static String getDescription(ErrorCode errorCode) {
		if ((errorCode != null) && (errorCode.getDescriptionResId() != null)) {
			return LocalizationUtils.getString(errorCode.getDescriptionResId());
		} else {
			return null;
		}
	}
	
	public static String getDescription(ErrorCode errorCode, Object... args) {
		if ((errorCode != null) && (errorCode.getDescriptionResId() != null)) {
			return LocalizationUtils.getString(errorCode.getDescriptionResId(), args);
		} else {
			return null;
		}
	}
}
