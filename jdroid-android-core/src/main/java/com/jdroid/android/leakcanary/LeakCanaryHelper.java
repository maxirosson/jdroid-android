package com.jdroid.android.leakcanary;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.jdroid.android.context.BuildConfigUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public class LeakCanaryHelper {
	
	private static RefWatcher refWatcher;
	
	public static void init(Context context) {
		Application application = (Application) context.getApplicationContext();
		refWatcher = LeakCanary.install(application);
	}
	
	public static void onFragmentDestroyView(Fragment fragment) {
		if (isLeakCanaryEnabled()) {
			RefWatcher refWatcher = LeakCanaryHelper.getRefWatcher();
			refWatcher.watch(fragment);
		}
	}
	
	public static RefWatcher getRefWatcher() {
		return refWatcher;
	}
	
	public static Boolean isLeakCanaryEnabled() {
		return BuildConfigUtils.getBuildConfigBoolean("LEAK_CANARY_ENABLED", false);
	}
}
