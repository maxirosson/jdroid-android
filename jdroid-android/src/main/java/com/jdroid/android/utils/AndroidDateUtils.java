package com.jdroid.android.utils;

import java.text.DateFormat;
import java.util.Date;
import com.jdroid.android.AbstractApplication;
import com.jdroid.java.utils.DateUtils;

/**
 * Date Utils that returns formatted times & dates according to the current locale and user preferences
 * 
 * @author Maxi Rosson
 */
public class AndroidDateUtils {
	
	/**
	 * @return The formatted time
	 */
	public static String formatTime() {
		return AndroidDateUtils.formatTime(DateUtils.now());
	}
	
	/**
	 * @param date The {@link Date} to format
	 * @return The formatted time
	 */
	public static String formatTime(Date date) {
		DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(AbstractApplication.get());
		return DateUtils.format(date, timeFormat);
	}
	
	/**
	 * @return The formatted date
	 */
	public static String formatDate() {
		return AndroidDateUtils.formatDate(DateUtils.now());
	}
	
	/**
	 * @param date The {@link Date} to format
	 * @return The formatted date
	 */
	public static String formatDate(Date date) {
		DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(AbstractApplication.get());
		return DateUtils.format(date, dateFormat);
	}
	
}
