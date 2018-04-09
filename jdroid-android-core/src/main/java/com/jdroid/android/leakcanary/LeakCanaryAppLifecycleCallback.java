package com.jdroid.android.leakcanary;

import android.content.Context;
import android.support.annotation.NonNull;

import com.jdroid.android.lifecycle.ApplicationLifecycleCallback;

public class LeakCanaryAppLifecycleCallback extends ApplicationLifecycleCallback {
	
	@Override
	public void onCreate(Context context) {
		LeakCanaryHelper.init(context);
	}
	
	@NonNull
	@Override
	public Integer getInitOrder() {
		return Integer.MAX_VALUE - 1;
	}
	
	@Override
	public Boolean isEnabled() {
		return LeakCanaryHelper.isLeakCanaryEnabled();
	}
}
