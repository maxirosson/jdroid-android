package com.jdroid.android.service;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.OneoffTask;
import com.google.android.gms.gcm.Task;
import com.google.android.gms.gcm.TaskParams;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.io.Serializable;

public abstract class ServiceCommand implements Serializable {

	private static final Logger LOGGER = LoggerUtils.getLogger(ServiceCommand.class);

	public void start() {
		start(null);
	}

	public void start(Intent intent) {
		LOGGER.info("Scheduling Worker Service for " + getClass().getSimpleName());

		intent = intent != null ? intent : new Intent();
		intent.putExtra(CommandWorkerService.COMMAND_EXTRA, getClass().getName());
		CommandWorkerService.runIntentInService(AbstractApplication.get(), intent, CommandWorkerService.class);
	}

	void startGcmTaskService(Bundle bundle) {
		LOGGER.info("Scheduling GCM Task Service for " + getClass().getSimpleName());

		Task.Builder builder = createBuilder();
		builder.setExtras(bundle);
		builder.setService(CommandGcmTaskService.class);
		GcmNetworkManager.getInstance(AbstractApplication.get()).schedule(builder.build());
	}

	protected Task.Builder createBuilder() {
		OneoffTask.Builder builder = new OneoffTask.Builder();
		builder.setPersisted(true);
		builder.setExecutionWindow(0, 5);
		builder.setTag(getClass().getSimpleName());
		return builder;
	}

	protected abstract int execute(Intent intent);

	protected abstract int executeRetry(TaskParams taskParams);
}
