package com.jdroid.android.firebase.jobdispatcher;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.service.WorkerService;
import com.jdroid.java.utils.LoggerUtils;

public class JobUtils {
	
	public static void startService(Bundle bundle, ServiceCommand serviceCommand, Boolean tryInstantExecution) {
		if (tryInstantExecution && !AbstractApplication.get().isInBackground()) {
			startWorkerService(bundle, serviceCommand);
		} else {
			startFirebaseJobService(bundle, serviceCommand);
		}
	}
	
	public static void startWorkerService(Bundle bundle, ServiceCommand serviceCommand) {
		LoggerUtils.getLogger(serviceCommand.getClass()).info("Starting Worker Service");
		Intent intent = new Intent();
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		intent.putExtra(ServiceCommand.COMMAND_EXTRA, serviceCommand.getClass().getName());
		WorkerService.runIntentInService(AbstractApplication.get(), intent, CommandWorkerService.class);
	}
	
	public static void startFirebaseJobService(Bundle bundle, ServiceCommand serviceCommand) {
		LoggerUtils.getLogger(serviceCommand.getClass()).info("Scheduling Firebase Job Service");
		
		if (bundle == null) {
			bundle = new Bundle();
		}
		bundle.putSerializable(ServiceCommand.COMMAND_EXTRA, serviceCommand.getClass().getName());
		
		FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(AbstractApplication.get()));
		Job.Builder builder = serviceCommand.createJobBuilder(dispatcher, bundle);
		builder.setService(CommandJobService.class);
		dispatcher.mustSchedule(builder.build());
	}
	
}
