package com.jdroid.android.firebase.jobdispatcher;


import android.support.annotation.WorkerThread;

import com.firebase.jobdispatcher.JobParameters;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.utils.ReflectionUtils;

// TODO Rename to CommandFirebaseJobService
public class CommandJobService extends AbstractJobService {
	
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
	
	@Override
	protected String getTag(JobParameters jobParameters) {
		String serviceCommandExtra = getServiceCommand(jobParameters);
		return serviceCommandExtra == null ? super.getTag(jobParameters) : serviceCommandExtra.substring(serviceCommandExtra.lastIndexOf(".") + 1);
	}
	
	private String getServiceCommand(JobParameters jobParameters) {
		return jobParameters.getExtras() != null ? jobParameters.getExtras().getString(ServiceCommand.COMMAND_EXTRA) : null;
	}
}
