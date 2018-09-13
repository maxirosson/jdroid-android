package com.jdroid.android.firebase.jobdispatcher;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.service.AbstractWorkerService;
import com.jdroid.java.http.exception.ConnectionException;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.ReflectionUtils;

public class CommandWorkerService extends AbstractWorkerService {

	public CommandWorkerService() {
		super(CommandWorkerService.class.getSimpleName());
	}

	@Override
	protected String getTag(@Nullable Intent intent) {
		String serviceCommandExtra = getServiceCommand(intent);
		return serviceCommandExtra == null ? super.getTag(intent) : serviceCommandExtra.substring(serviceCommandExtra.lastIndexOf(".") + 1);
	}

	@Override
	protected void doExecute(@NonNull Intent intent) {
		String serviceCommandExtra = getServiceCommand(intent);
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
				serviceCommand.startFirebaseJobService(intent.getExtras());
			}
		} else {
			AbstractApplication.get().getExceptionHandler().logWarningException("Service command not found on " + getClass().getSimpleName());
		}
	}

	private String getServiceCommand(@Nullable Intent intent) {
		return intent != null ? intent.getStringExtra(ServiceCommand.COMMAND_EXTRA) : null;
	}

	protected Boolean needsReschedule(Throwable throwable) {
		return throwable instanceof ConnectionException;
	}
}
