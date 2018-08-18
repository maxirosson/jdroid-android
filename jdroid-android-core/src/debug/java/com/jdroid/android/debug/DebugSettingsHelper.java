package com.jdroid.android.debug;

import com.jdroid.java.collections.Lists;

import java.util.List;

public class DebugSettingsHelper {

	private static List<PreferencesAppender> preferencesAppenders = Lists.newArrayList();

	public static List<PreferencesAppender> getPreferencesAppenders() {
		return preferencesAppenders;
	}

	public static void addPreferencesAppender(PreferencesAppender preferencesAppender) {
		preferencesAppenders.add(preferencesAppender);
	}
}
