package com.jdroid.android.debug;

import android.app.Activity;
import android.support.v4.util.Pair;

import com.jdroid.android.sqlite.SQLiteHelper;
import com.jdroid.java.domain.Identifiable;
import com.jdroid.java.repository.Repository;

import java.util.List;
import java.util.Map;

public class DebugContext {

	public void launchActivityDebugSettingsActivity(Activity activity) {
		// Do nothing
	}

	public void initDebugRepositories(
		Map<Class<? extends Identifiable>, Repository<? extends Identifiable>> repositories, SQLiteHelper dbHelper) {
		// Do nothing
	}

	public List<Pair<String, Object>> getCustomDebugInfoProperties() {
		return null;
	}

	public void addCustomDebugInfoProperty(Pair<String, Object> pair) {
		// Do nothing
	}
}
