package com.jdroid.android.service;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.TaskParams;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.utils.ReflectionUtils;

public class CommandGcmTaskService extends AbstractGcmTaskService {

	public static final String COMMAND_EXTRA = "command";

	@Override
	public void onInitializeTasks() {
		super.onInitializeTasks();
		AbstractApplication.get().initializeGcmTasks();
	}

	@Override
	protected String getTrackingLabel(TaskParams taskParams) {
		String serviceCommandExtra = taskParams.getExtras() != null ? taskParams.getExtras().getString(COMMAND_EXTRA) : null;
		return serviceCommandExtra == null ? getTrackingVariable(taskParams) : serviceCommandExtra.substring(serviceCommandExtra.lastIndexOf(".") + 1);
	}

	@Override
	public int doRunTask(TaskParams taskParams) {
		String serviceCommandExtra = taskParams.getExtras() != null ? taskParams.getExtras().getString(COMMAND_EXTRA) : null;
		if (serviceCommandExtra != null) {
			ServiceCommand serviceCommand = ReflectionUtils.newInstance(serviceCommandExtra);
			return serviceCommand.executeRetry(taskParams);
		} else {
			AbstractApplication.get().getExceptionHandler().logWarningException("Service command not found on " + getClass().getSimpleName());
			return GcmNetworkManager.RESULT_FAILURE;
		}
	}
}
