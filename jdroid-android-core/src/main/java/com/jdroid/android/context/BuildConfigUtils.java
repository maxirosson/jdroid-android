package com.jdroid.android.context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BuildConfigUtils {

	private static BuildConfigResolver buildConfigResolver = new BuildConfigResolver();
	private static Map<String, Object> cache = new ConcurrentHashMap<>();

	@SuppressWarnings("unchecked")
	public static <T> T getBuildConfigValue(@NonNull String property) {
		return (T)getBuildConfigValue(property, null);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBuildConfigValue(@NonNull String property, @Nullable Object defaultValue) {
		Object value = cache.get(property);
		if (value == null) {
			value = buildConfigResolver.getBuildConfigValue(property, defaultValue);
			if (value != null) {
				cache.put(property, value);
			}
		}
		return (T)value;
	}

	public static String getBuildConfigString(@NonNull String property) {
		return (String)getBuildConfigValue(property);
	}

	public static Boolean getBuildConfigBoolean(@NonNull String property, @Nullable Boolean defaultValue) {
		return (Boolean)getBuildConfigValue(property, defaultValue);
	}

	public static void setBuildConfigResolver(@NonNull BuildConfigResolver buildConfigResolver) {
		BuildConfigUtils.buildConfigResolver = buildConfigResolver;
	}
}
