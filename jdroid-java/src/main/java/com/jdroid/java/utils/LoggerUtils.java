package com.jdroid.java.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jdroid.java.logger.MuteLogger;

/**
 * 
 * @author Maxi Rosson
 */
public class LoggerUtils {
	
	private static boolean release = true;
	
	private static final Logger MUTE_LOGGER = new MuteLogger();
	
	public static Logger getLogger(Object name) {
		return LoggerUtils.getLogger(name.getClass());
	}
	
	public static Logger getLogger(Class<?> clazz) {
		if (release) {
			return MUTE_LOGGER;
		} else {
			return LoggerFactory.getLogger(getLmitedName(clazz.getSimpleName()));
		}
	}
	
	private static String getLmitedName(String name) {
		// Logcat support 23 characters as maximum
		return name.substring(0, Math.min(name.length(), 23));
	}
	
	public static void setRelease(boolean release) {
		LoggerUtils.release = release;
	}
}
