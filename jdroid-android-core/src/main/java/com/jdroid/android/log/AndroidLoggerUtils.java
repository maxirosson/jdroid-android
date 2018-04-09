package com.jdroid.android.log;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

public class AndroidLoggerUtils {

	public static Logger getDatabaseLogger(Class<?> clazz) {
		if (LoggerUtils.isEnabled() && AbstractApplication.get().isDatabaseEnabled()) {
			return new DatabaseLogger(LoggerUtils.getLogger(clazz));
		} else {
			return LoggerUtils.getLogger(clazz);
		}
	}
}
