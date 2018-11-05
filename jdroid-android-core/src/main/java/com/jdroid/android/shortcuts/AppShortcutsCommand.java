package com.jdroid.android.shortcuts;

import android.content.Context;
import android.os.Bundle;

import com.jdroid.android.firebase.jobdispatcher.ServiceCommand;

public class AppShortcutsCommand extends ServiceCommand {

	@Override
	public void start() {
		if (AppShortcutsHelper.isDynamicAppShortcutsSupported()) {
			super.start();
		}
	}

	@Override
	protected boolean execute(Context context, Bundle bundle) {
		DynamicShortcutsLoader dynamicShortcutsLoader = AppShortcutsHelper.getDynamicShortcutsLoader();
		AppShortcutsHelper.setDynamicShortcuts(dynamicShortcutsLoader.loadDynamicShortcuts());
		return false;
	}
}
