package com.jdroid.android.shortcuts;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;

import com.jdroid.android.firebase.jobdispatcher.ServiceCommand;
import com.jdroid.android.utils.AndroidUtils;

public class AppShortcutsCommand extends ServiceCommand {

	@Override
	public void start() {
		if (AndroidUtils.getApiLevel() >= Build.VERSION_CODES.N_MR1) {
			super.start();
		}
	}

	@RequiresApi(api = Build.VERSION_CODES.N_MR1)
	@Override
	protected boolean execute(Context context, Bundle bundle) {
		AppShortcutsHelper.setDynamicShortcuts(AppShortcutsHelper.getInitialShortcutInfos());
		return false;
	}
}
