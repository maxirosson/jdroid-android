package com.jdroid.android.debug.info;

import androidx.core.util.Pair;

import java.util.List;

public interface DebugInfoAppender {

	public List<Pair<String, Object>> getDebugInfoProperties();
}
