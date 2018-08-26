package com.jdroid.android.debug.info;

import com.jdroid.java.collections.Lists;

import java.util.List;

public class DebugInfoHelper {

	private static List<DebugInfoAppender> debugInfoAppenders = Lists.newArrayList();

	public static List<DebugInfoAppender> getDebugInfoAppenders() {
		return debugInfoAppenders;
	}

	public static void addDebugInfoAppender(DebugInfoAppender debugInfoAppender) {
		debugInfoAppenders.add(debugInfoAppender);
	}

}
