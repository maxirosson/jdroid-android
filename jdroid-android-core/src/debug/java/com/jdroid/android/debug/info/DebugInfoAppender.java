package com.jdroid.android.debug.info;

import android.support.v4.util.Pair;

import java.util.List;

public interface DebugInfoAppender {
	
	public List<Pair<String, Object>> getDebugInfoProperties();
}
