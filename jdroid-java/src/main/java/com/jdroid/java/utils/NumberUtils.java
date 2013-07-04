package com.jdroid.java.utils;

/**
 * 
 * @author Maxi Rosson
 */
public class NumberUtils {
	
	public static Float getFloat(String value) {
		return getFloat(value, null);
	}
	
	public static Float getFloat(String value, Float defaultValue) {
		return StringUtils.isNotEmpty(value) ? Float.valueOf(value) : defaultValue;
	}
	
	public static Integer getInteger(String value) {
		return getInteger(value, null);
	}
	
	public static Integer getInteger(String value, Integer defaultValue) {
		return StringUtils.isNotEmpty(value) ? Integer.valueOf(value) : defaultValue;
	}
	
	public static Integer getSafeInteger(String value) {
		try {
			return getInteger(value);
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	public static Long getLong(String value) {
		return StringUtils.isNotEmpty(value) ? Long.valueOf(value) : null;
	}
	
	public static Long getSafeLong(String value) {
		try {
			return getLong(value);
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	public static Double getDouble(String value) {
		return StringUtils.isNotEmpty(value) ? Double.valueOf(value) : null;
	}
	
	public static Boolean getBooleanFromNumber(String value) {
		return StringUtils.isNotEmpty(value) ? "1".equals(value) : null;
	}
	
	public static Boolean getBoolean(String value) {
		return getBoolean(value, null);
	}
	
	public static Boolean getBoolean(String value, Boolean defaultValue) {
		return StringUtils.isNotEmpty(value) ? Boolean.parseBoolean(value) : defaultValue;
	}
	
	public static String getString(Integer value) {
		return value != null ? value.toString() : null;
	}
	
	public static String getOrdinalSuffix(int n) {
		// REVIEW Add internationalization support
		if ((n >= 11) && (n <= 13)) {
			return "th";
		}
		switch (n % 10) {
			case 1:
				return "st";
			case 2:
				return "nd";
			case 3:
				return "rd";
			default:
				return "th";
		}
	}
}
