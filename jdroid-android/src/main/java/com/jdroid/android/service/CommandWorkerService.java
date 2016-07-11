package com.jdroid.android.service;

import android.content.Intent;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.ReflectionUtils;

import org.slf4j.Logger;

public class CommandWorkerService extends WorkerService {

	private static final Logger LOGGER = LoggerUtils.getLogger(CommandWorkerService.class);

	public static final String COMMAND_EXTRA = "command";

	@Override
	protected String getTrackingLabel(Intent intent) {
		String serviceCommandExtra = intent.getStringExtra(COMMAND_EXTRA);
		return serviceCommandExtra == null ? getTrackingVariable(intent) : serviceCommandExtra.substring(serviceCommandExtra.lastIndexOf(".") + 1);
	}

	@Override
	protected void doExecute(Intent intent) {
		String serviceCommandExtra = intent.getStringExtra(COMMAND_EXTRA);
		if (serviceCommandExtra != null) {
			ServiceCommand serviceCommand = ReflectionUtils.newInstance(serviceCommandExtra);
			int result = serviceCommand.execute(intent);
			LOGGER.info(serviceCommand.getClass().getSimpleName() + " executed with result " + result);
			if (result == GcmNetworkManager.RESULT_RESCHEDULE) {
				serviceCommand.startGcmTaskService(intent.getExtras());
			}
		} else {
			AbstractApplication.get().getExceptionHandler().logWarningException("Service command not found on " + getClass().getSimpleName());
		}
	}
}
