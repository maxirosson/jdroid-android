package com.jdroid.android.sample.application.lifecycle;

import android.content.Context;
import android.support.annotation.NonNull;

import com.jdroid.android.lifecycle.ApplicationLifecycleCallback;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;


public class SampleApplicationLifecycleCallback extends ApplicationLifecycleCallback {

	private static final Logger LOGGER = LoggerUtils.getLogger(SampleApplicationLifecycleCallback.class);

	@Override
	public void onProviderInit(Context context) {
		LOGGER.debug("Executing sample onProviderInit");
	}

	@Override
	public void onCreate(Context context) {
		LOGGER.debug("Executing sample onCreate");
	}

	@NonNull
	@Override
	public Integer getInitOrder() {
		return -1;
	}
}
