package com.jdroid.android.utils;

import androidx.annotation.NonNull;

public class TagUtils {

	@NonNull
	public static String getTag(Class clazz) {
		String tag;
		if (clazz.getEnclosingClass() != null) {
			tag = clazz.getEnclosingClass().getSimpleName();
		} else {
			tag = clazz.getSimpleName();
		}
		return tag;
	}
}
