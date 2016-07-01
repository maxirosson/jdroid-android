package com.jdroid.android.log;

import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

public class AndroidLoggerUtils {

	private AndroidLoggerUtils(){}

	public static Logger getDatabaseLogger(Class<?> clazz) {
		if (LoggerUtils.isEnabled()) {
			return new DatabaseLogger(LoggerUtils.getLogger(clazz));
		} else {
			return LoggerUtils.getLogger(clazz);
		}
	}
}
