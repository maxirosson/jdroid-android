package com.jdroid.android.leakcanary;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.jdroid.android.context.BuildConfigUtils;
import com.squareup.leakcanary.LeakCanary;

public class LeakCanaryHelper {
	
	public static void init(Context context) {
		LeakCanary.install((Application) context.getApplicationContext());
	}
	
	public static @NonNull Boolean isLeakCanaryEnabled() {
		return BuildConfigUtils.getBuildConfigBoolean("LEAK_CANARY_ENABLED", false);
	}
}
