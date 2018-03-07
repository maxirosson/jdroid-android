package com.jdroid.android.firebase.jobdispatcher;


import android.support.annotation.WorkerThread;

import com.firebase.jobdispatcher.JobParameters;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.utils.ReflectionUtils;

public class CommandJobService extends AbstractJobService {
	
	@Override
	protected String getTrackingLabel(JobParameters jobParameters) {
		String serviceCommandExtra = getServiceCommand(jobParameters);
		return serviceCommandExtra == null ? getTrackingVariable(jobParameters) : serviceCommandExtra.substring(serviceCommandExtra.lastIndexOf(".") + 1);
	}
	
	@WorkerThread
	@Override
	public boolean onRunJob(JobParameters jobParameters) {
		String serviceCommandExtra = getServiceCommand(jobParameters);
		if (serviceCommandExtra != null) {
			ServiceCommand serviceCommand = ReflectionUtils.newInstance(serviceCommandExtra);
			return serviceCommand.execute(this, jobParameters.getExtras());
		} else {
			AbstractApplication.get().getExceptionHandler().logWarningException("Service command not found on " + getClass().getSimpleName());
			return false;
		}
	}
	
	private String getServiceCommand(JobParameters jobParameters) {
		return jobParameters.getExtras() != null ? jobParameters.getExtras().getString(ServiceCommand.COMMAND_EXTRA) : null;
	}
}
