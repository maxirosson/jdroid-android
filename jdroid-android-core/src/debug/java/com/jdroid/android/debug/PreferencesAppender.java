package com.jdroid.android.debug;

import android.app.Activity;
import android.preference.PreferenceGroup;

import com.jdroid.java.collections.Lists;

import java.io.Serializable;
import java.util.List;

public abstract class PreferencesAppender implements Serializable {

	public abstract int getNameResId();

	public abstract void initPreferences(Activity activity, PreferenceGroup preferenceGroup);

	public Boolean isEnabled() {
		return true;
	}

	public List<String> getRequiredPermissions() {
		return Lists.newArrayList();
	}

	public String getSharedPreferencesName() {
		return null;
	}
}
