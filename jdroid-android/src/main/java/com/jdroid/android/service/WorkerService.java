package com.jdroid.android.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

public abstract class WorkerService extends IntentService {

	private static final Logger LOGGER = LoggerUtils.getLogger(WorkerService.class);

	private static String TAG = WorkerService.class.getSimpleName();

	public WorkerService() {
		super(TAG);
	}
	
	public WorkerService(String name) {
		super(name);
	}
	
	@Override
	protected final void onHandleIntent(Intent intent) {
		if (intent != null) {
			String trackingVariable = getTrackingVariable(intent);
			String trackingLabel = getTrackingLabel(intent);
			LOGGER.info("Starting service. Variable: " + trackingVariable + " - Label: " + trackingLabel);
			try {
					long startTime = DateUtils.nowMillis();
					doExecute(intent);
					if (enableTimingTracking()) {
						long executionTime = DateUtils.nowMillis() - startTime;
						AbstractApplication.get().getAnalyticsSender().trackServiceTiming(getTrackingVariable(intent),
								getTrackingLabel(intent), executionTime);
					}
			} catch (Exception e) {
				AbstractApplication.get().getExceptionHandler().logHandledException(e);
			}
		} else {
			AbstractApplication.get().getExceptionHandler().logWarningException(
				"Null intent when starting the service: " + getClass().getName());
		}
	}

	protected Boolean enableTimingTracking() {
		return true;
	}

	protected String getTrackingVariable(Intent intent) {
		return getClass().getSimpleName();
	}

	protected String getTrackingLabel(Intent intent) {
		return getClass().getSimpleName();
	}

	protected abstract void doExecute(Intent intent);
	
	protected static void runIntentInService(Context context, Intent intent, Class<? extends WorkerService> serviceClass) {
		context.startService(getServiceIntent(context, intent, serviceClass));
	}
	
	protected static Intent getServiceIntent(Context context, Intent intent, Class<? extends WorkerService> serviceClass) {
		intent.setClass(context, serviceClass);
		return intent;
	}
	
}
