package com.jdroid.android.utils;

import java.util.Map;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.jdroid.android.AbstractApplication;

/**
 * Utils to work with the shared preferences
 * 
 * @author Maxi Rosson
 */
public class SharedPreferencesUtils {
	
	private static final String DEFAULT_NAME = AbstractApplication.get().getPackageName();
	
	public static void savePreference(String key, String value) {
		if (value != null) {
			Editor editor = getEditor();
			editor.putString(key, value);
			
			// Commit the edits!
			editor.commit();
		}
	}
	
	public static void savePreference(String key, Boolean value) {
		if (value != null) {
			Editor editor = getEditor();
			editor.putBoolean(key, value);
			
			// Commit the edits!
			editor.commit();
		}
	}
	
	public static void savePreference(String key, Integer value) {
		if (value != null) {
			Editor editor = getEditor();
			editor.putInt(key, value);
			
			// Commit the edits!
			editor.commit();
		}
	}
	
	public static void savePreference(String key, Long value) {
		if (value != null) {
			Editor editor = getEditor();
			editor.putLong(key, value);
			
			// Commit the edits!
			editor.commit();
		}
	}
	
	public static void savePreference(String key, Float value) {
		if (value != null) {
			Editor editor = getEditor();
			editor.putFloat(key, value);
			
			// Commit the edits!
			editor.commit();
		}
	}
	
	public static Editor getEditor() {
		return getSharedPreferences().edit();
	}
	
	public static Editor getEditor(String name) {
		return getSharedPreferences(name).edit();
	}
	
	private static SharedPreferences getSharedPreferences(String name) {
		return AbstractApplication.get().getSharedPreferences(name, Context.MODE_PRIVATE);
	}
	
	private static SharedPreferences getSharedPreferences() {
		return getSharedPreferences(DEFAULT_NAME);
	}
	
	/**
	 * Retrieves all the existent shared preferences.
	 * 
	 * @return The shared preferences.
	 */
	public static Map<String, ?> loadAllPreferences() {
		return getSharedPreferences().getAll();
	}
	
	/**
	 * Retrieve a string value from the preferences.
	 * 
	 * @param key The name of the preference to retrieve
	 * @param defaultValue Value to return if this preference does not exist
	 * @return the preference value if it exists, or defaultValue.
	 */
	public static String loadPreference(String key, String defaultValue) {
		return getSharedPreferences().getString(key, defaultValue);
	}
	
	/**
	 * Retrieve a string value from the preferences.
	 * 
	 * @param key The name of the preference to retrieve
	 * @return the preference value if it exists, or null.
	 */
	public static String loadPreference(String key) {
		return getSharedPreferences().getString(key, (String)null);
	}
	
	/**
	 * Retrieve a boolean value from the preferences.
	 * 
	 * @param key The name of the preference to retrieve
	 * @param defaultValue Value to return if this preference does not exist
	 * @return the preference value if it exists, or defaultValue.
	 */
	public static Boolean loadPreferenceAsBoolean(String key, Boolean defaultValue) {
		Boolean value = defaultValue;
		if (hasPreference(key)) {
			value = getSharedPreferences().getBoolean(key, false);
		}
		return value;
	}
	
	/**
	 * Retrieve a boolean value from the preferences.
	 * 
	 * @param key The name of the preference to retrieve
	 * @return the preference value if it exists, or null.
	 */
	public static Boolean loadPreferenceAsBoolean(String key) {
		return loadPreferenceAsBoolean(key, null);
	}
	
	/**
	 * Retrieve a long value from the preferences.
	 * 
	 * @param key The name of the preference to retrieve
	 * @param defaultValue Value to return if this preference does not exist
	 * @return the preference value if it exists, or defaultValue.
	 */
	public static Long loadPreferenceAsLong(String key, Long defaultValue) {
		Long value = defaultValue;
		if (hasPreference(key)) {
			value = getSharedPreferences().getLong(key, 0L);
		}
		return value;
		
	}
	
	/**
	 * Retrieve a long value from the preferences.
	 * 
	 * @param key The name of the preference to retrieve
	 * @return the preference value if it exists, or null.
	 */
	public static Long loadPreferenceAsLong(String key) {
		return loadPreferenceAsLong(key, null);
	}
	
	/**
	 * Retrieve an Integer value from the preferences.
	 * 
	 * @param key The name of the preference to retrieve
	 * @param defaultValue Value to return if this preference does not exist
	 * @return the preference value if it exists, or defaultValue.
	 */
	public static Integer loadPreferenceAsInteger(String key, Integer defaultValue) {
		Integer value = defaultValue;
		if (hasPreference(key)) {
			value = getSharedPreferences().getInt(key, 0);
		}
		return value;
	}
	
	/**
	 * Retrieve an Integer value from the preferences.
	 * 
	 * @param key The name of the preference to retrieve
	 * @return the preference value if it exists, or null.
	 */
	public static Integer loadPreferenceAsInteger(String key) {
		return loadPreferenceAsInteger(key, null);
	}
	
	/**
	 * Retrieve a Float value from the preferences.
	 * 
	 * @param key The name of the preference to retrieve
	 * @param defaultValue Value to return if this preference does not exist
	 * @return the preference value if it exists, or defaultValue.
	 */
	public static Float loadPreferenceAsFloat(String key, Float defaultValue) {
		Float value = defaultValue;
		if (hasPreference(key)) {
			value = getSharedPreferences().getFloat(key, 0);
		}
		return value;
	}
	
	/**
	 * Retrieve a Float value from the preferences.
	 * 
	 * @param key The name of the preference to retrieve
	 * @return the preference value if it exists, or null.
	 */
	public static Float loadPreferenceAsFloat(String key) {
		return loadPreferenceAsFloat(key, null);
	}
	
	public static boolean hasPreference(String key) {
		return getSharedPreferences().contains(key);
	}
	
	public static void removePreferences(String... keys) {
		Editor editor = getEditor();
		for (String key : keys) {
			editor.remove(key);
		}
		// Commit the edits!
		editor.commit();
	}
	
	public static void removeAllPreferences(String name) {
		Editor editor = getEditor(name);
		editor.clear();
		
		// Commit the edits!
		editor.commit();
	}
	
	public static void removeAllPreferences() {
		removeAllPreferences(DEFAULT_NAME);
	}
}
