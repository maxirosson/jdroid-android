package com.jdroid.android.shortcuts;

import android.content.pm.ShortcutInfo;

import java.util.List;

import androidx.annotation.WorkerThread;

public interface DynamicShortcutsLoader {

	@WorkerThread
	List<ShortcutInfo> loadDynamicShortcuts();
}
