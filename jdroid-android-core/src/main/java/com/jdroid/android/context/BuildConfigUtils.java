package com.jdroid.android.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BuildConfigUtils {
	
	private static BuildConfigResolver buildConfigResolver = new BuildConfigResolver();
	private static Map<String, Object> cache = new ConcurrentHashMap<>();

	@SuppressWarnings("unchecked")
	public static <T> T getBuildConfigValue(String property) {
		return (T)getBuildConfigValue(property, null);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBuildConfigValue(String property, Object defaultValue) {
		Object value = cache.get(property);
		if (value == null) {
			value = buildConfigResolver.getBuildConfigValue(property, defaultValue);
			if (value != null) {
				cache.put(property, value);
			}
		}
		return (T)value;
	}
	
	public static String getBuildConfigString(String property) {
		return (String)getBuildConfigValue(property);
	}
	
	public static Boolean getBuildConfigBoolean(String property, Boolean defaultValue) {
		return (Boolean)getBuildConfigValue(property, defaultValue);
	}
	
	public static void setBuildConfigResolver(BuildConfigResolver buildConfigResolver) {
		BuildConfigUtils.buildConfigResolver = buildConfigResolver;
	}
}
