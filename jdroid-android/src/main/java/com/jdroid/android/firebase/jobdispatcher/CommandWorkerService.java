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
	protected String getTag(Intent intent) {
		String serviceCommandExtra = intent.getStringExtra(ServiceCommand.COMMAND_EXTRA);
		return serviceCommandExtra == null ? super.getTag(intent) : serviceCommandExtra.substring(serviceCommandExtra.lastIndexOf(".") + 1);
	}

	@Override
	protected void doExecute(Intent intent) {
		String serviceCommandExtra = intent.getStringExtra(ServiceCommand.COMMAND_EXTRA);
		if (serviceCommandExtra != null) {
			ServiceCommand serviceCommand = ReflectionUtils.newInstance(serviceCommandExtra);
			Boolean needsReschedule;
			try {
				needsReschedule = serviceCommand.execute(this, intent.getExtras());
				LoggerUtils.getLogger(serviceCommand.getClass()).info("Service command finished successfully. NeedsReschedule: " + needsReschedule);
			} catch (Exception e) {
				needsReschedule = needsReschedule(e);
				LoggerUtils.getLogger(serviceCommand.getClass()).error("Service command finished with error. NeedsReschedule: " + needsReschedule);
				AbstractApplication.get().getExceptionHandler().logHandledException(e);
			}
			if (needsReschedule) {
				JobUtils.startFirebaseJobService(intent.getExtras(), serviceCommand);
			}
		} else {
			AbstractApplication.get().getExceptionHandler().logWarningException("Service command not found on " + getClass().getSimpleName());
		}
	}
	
	protected Boolean needsReschedule(Throwable throwable) {
		return throwable instanceof ConnectionException;
	}
}
