package com.jdroid.android.firebase.jobdispatcher;

import android.content.Intent;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.service.WorkerService;
import com.jdroid.java.http.exception.ConnectionException;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.ReflectionUtils;

import org.slf4j.Logger;

public class CommandWorkerService extends WorkerService {

	private final static Logger LOGGER = LoggerUtils.getLogger(CommandWorkerService.class);

	@Override
	protected String getTrackingLabel(Intent intent) {
		String serviceCommandExtra = intent.getStringExtra(ServiceCommand.COMMAND_EXTRA);
		return serviceCommandExtra == null ? getTrackingVariable(intent) : serviceCommandExtra.substring(serviceCommandExtra.lastIndexOf(".") + 1);
	}

	@Override
	protected void doExecute(Intent intent) {
		String serviceCommandExtra = intent.getStringExtra(ServiceCommand.COMMAND_EXTRA);
		if (serviceCommandExtra != null) {
			ServiceCommand serviceCommand = ReflectionUtils.newInstance(serviceCommandExtra);
			Boolean needsReschedule;
			try {
				needsReschedule = serviceCommand.execute(this, intent.getExtras());
				LOGGER.info(serviceCommand.getClass().getSimpleName() + " executed. NeedsReschedule: " + needsReschedule);
			} catch (Exception e) {
				needsReschedule = needsReschedule(e);
				LOGGER.error(serviceCommand.getClass().getSimpleName() + " finished with error. NeedsReschedule: " + needsReschedule);
				AbstractApplication.get().getExceptionHandler().logHandledException(e);
			}
			if (needsReschedule) {
				JobUtils.startJobService(intent.getExtras(), serviceCommand);
			}
		} else {
			AbstractApplication.get().getExceptionHandler().logWarningException("Service command not found on " + getClass().getSimpleName());
		}
	}
	
	protected Boolean needsReschedule(Throwable throwable) {
		return throwable instanceof ConnectionException;
	}
}
