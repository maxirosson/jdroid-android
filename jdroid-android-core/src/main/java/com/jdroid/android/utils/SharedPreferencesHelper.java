package com.jdroid.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.StringUtils;

import org.slf4j.Logger;

import java.util.List;
import java.util.Map;

import androidx.annotation.WorkerThread;

/**
 * Helper to work with the shared preferences
 */
public class SharedPreferencesHelper {

	private static final Logger LOGGER = LoggerUtils.getLogger(SharedPreferencesHelper.class);

	private static SharedPreferencesHelper defaultSharedPreferencesHelper = new SharedPreferencesHelper(null);

	private String name;
	private SharedPreferences sharedPreferences;

	public static SharedPreferencesHelper get(Class<?> clazz) {
		return get(clazz.getSimpleName());
	}

	public static SharedPreferencesHelper get(String name) {
		return new SharedPreferencesHelper(name);
	}

	public static SharedPreferencesHelper get() {
		return defaultSharedPreferencesHelper;
	}

	public SharedPreferencesHelper(String name) {
		this.name = name;
	}

	public Editor getEditor() {
		return getSharedPreferences().edit();
	}

	@WorkerThread
	public SharedPreferences getSharedPreferences() {
		if (sharedPreferences == null) {
			if (name != null) {
				sharedPreferences = AbstractApplication.get().getSharedPreferences(name, Context.MODE_PRIVATE);
			} else {
				sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get());
			}
		}
		return sharedPreferences;
	}

	private void logSave(String key, Object value) {
		if (name != null) {
			LOGGER.info("Saved [" + name + "] preference. Key [" + key + "] Value [" + value + "]");
		} else {
			LOGGER.info("Saved preference. Key [" + key + "] Value [" + value + "]");
		}
	}

	public void savePreference(String key, String value) {
		Editor editor = getEditor();
		editor.putString(key, value);
		editor.commit();
		logSave(key, value);
	}

	public void savePreferenceAsync(String key, String value) {
		Editor editor = getEditor();
		editor.putString(key, value);
		editor.apply();
		logSave(key, value);
	}

	public void savePreference(String key, Boolean value) {
		Editor editor = getEditor();
		editor.putBoolean(key, value);
		editor.commit();
		logSave(key, value);
	}

	public void savePreferenceAsync(String key, Boolean value) {
		Editor editor = getEditor();
		editor.putBoolean(key, value);
		editor.apply();
		logSave(key, value);
	}

	public void savePreference(String key, Integer value) {
		Editor editor = getEditor();
		editor.putInt(key, value);
		editor.commit();
		logSave(key, value);
	}

	public void savePreferenceAsync(String key, Integer value) {
		Editor editor = getEditor();
		editor.putInt(key, value);
		editor.apply();
		logSave(key, value);
	}

	public void savePreference(String key, Long value) {
		Editor editor = getEditor();
		editor.putLong(key, value);
		editor.commit();
		logSave(key, value);
	}

	public void savePreferenceAsync(String key, Long value) {
		Editor editor = getEditor();
		editor.putLong(key, value);
		editor.apply();
		logSave(key, value);
	}

	public void savePreference(String key, Float value) {
		Editor editor = getEditor();
		editor.putFloat(key, value);
		editor.commit();
		logSave(key, value);
	}

	public void savePreference(String key, List<String> values) {
		savePreference(key, StringUtils.join(values));
	}

	public void savePreferenceAsync(String key, Float value) {
		Editor editor = getEditor();
		editor.putFloat(key, value);
		editor.apply();
		logSave(key, value);
	}

	public void savePreferenceAsync(String key, List<String> values) {
		savePreferenceAsync(key, StringUtils.join(values));
	}

	public void appendPreferenceAsync(String key, String value) {
		List<String> values = loadPreferenceAsStringList(key);
		values.add(value);
		savePreferenceAsync(key, values);
	}

	/**
	 * Retrieves all the existent shared preferences.
	 *
	 * @return The shared preferences.
	 */
	public Map<String, ?> loadAllPreferences() {
		return getSharedPreferences().getAll();
	}

	private void logLoad(String key, Object value) {
		if (name != null) {
			LOGGER.info("Loaded [" + name + "] preference. Key [" + key + "] Value [" + value + "]");
		} else {
			LOGGER.info("Loaded preference. Key [" + key + "] Value [" + value + "]");
		}
	}

	/**
	 * Retrieve a string value from the preferences.
	 *
	 * @param key The name of the preference to retrieve
	 * @param defaultValue Value to return if this preference does not exist
	 * @return the preference value if it exists, or defaultValue.
	 */
	public String loadPreference(String key, String defaultValue) {
		String value = getSharedPreferences().getString(key, defaultValue);
		logLoad(key, value);
		return value;
	}

	/**
	 * Retrieve a string value from the preferences.
	 *
	 * @param key The name of the preference to retrieve
	 * @return the preference value if it exists, or null.
	 */
	public String loadPreference(String key) {
		return loadPreference(key, null);
	}

	/**
	 * Retrieve a boolean value from the preferences.
	 *
	 * @param key The name of the preference to retrieve
	 * @param defaultValue Value to return if this preference does not exist
	 * @return the preference value if it exists, or defaultValue.
	 */
	public Boolean loadPreferenceAsBoolean(String key, Boolean defaultValue) {
		Boolean value = defaultValue;
		if (hasPreference(key)) {
			value = getSharedPreferences().getBoolean(key, false);
		}
		logLoad(key, value);
		return value;
	}

	/**
	 * Retrieve a boolean value from the preferences.
	 *
	 * @param key The name of the preference to retrieve
	 * @return the preference value if it exists, or null.
	 */
	public Boolean loadPreferenceAsBoolean(String key) {
		return loadPreferenceAsBoolean(key, null);
	}

	/**
	 * Retrieve a long value from the preferences.
	 *
	 * @param key The name of the preference to retrieve
	 * @param defaultValue Value to return if this preference does not exist
	 * @return the preference value if it exists, or defaultValue.
	 */
	public Long loadPreferenceAsLong(String key, Long defaultValue) {
		Long value = defaultValue;
		if (hasPreference(key)) {
			value = getSharedPreferences().getLong(key, 0L);
		}
		logLoad(key, value);
		return value;

	}

	/**
	 * Retrieve a long value from the preferences.
	 *
	 * @param key The name of the preference to retrieve
	 * @return the preference value if it exists, or null.
	 */
	public Long loadPreferenceAsLong(String key) {
		return loadPreferenceAsLong(key, null);
	}

	/**
	 * Retrieve an Integer value from the preferences.
	 *
	 * @param key The name of the preference to retrieve
	 * @param defaultValue Value to return if this preference does not exist
	 * @return the preference value if it exists, or defaultValue.
	 */
	public Integer loadPreferenceAsInteger(String key, Integer defaultValue) {
		Integer value = defaultValue;
		if (hasPreference(key)) {
			value = getSharedPreferences().getInt(key, 0);
		}
		logLoad(key, value);
		return value;
	}

	/**
	 * Retrieve an Integer value from the preferences.
	 *
	 * @param key The name of the preference to retrieve
	 * @return the preference value if it exists, or null.
	 */
	public Integer loadPreferenceAsInteger(String key) {
		return loadPreferenceAsInteger(key, null);
	}

	/**
	 * Retrieve a Float value from the preferences.
	 *
	 * @param key The name of the preference to retrieve
	 * @param defaultValue Value to return if this preference does not exist
	 * @return the preference value if it exists, or defaultValue.
	 */
	public Float loadPreferenceAsFloat(String key, Float defaultValue) {
		Float value = defaultValue;
		if (hasPreference(key)) {
			value = getSharedPreferences().getFloat(key, 0);
		}
		logLoad(key, value);
		return value;
	}

	/**
	 * Retrieve a Float value from the preferences.
	 *
	 * @param key The name of the preference to retrieve
	 * @return the preference value if it exists, or null.
	 */
	public Float loadPreferenceAsFloat(String key) {
		return loadPreferenceAsFloat(key, null);
	}

	public List<String> loadPreferenceAsStringList(String key) {
		String value = loadPreference(key);
		if (value != null) {
			return StringUtils.splitWithCommaSeparator(value);
		}
		return Lists.INSTANCE.newArrayList();
	}

	public boolean hasPreference(String key) {
		return getSharedPreferences().contains(key);
	}

	public void removePreferences(String... keys) {
		Editor editor = getEditor();
		for (String key : keys) {
			editor.remove(key);
		}
		editor.commit();
	}

	public void removeAllPreferences() {
		Editor editor = getEditor();
		editor.clear();
		editor.commit();
	}

}
