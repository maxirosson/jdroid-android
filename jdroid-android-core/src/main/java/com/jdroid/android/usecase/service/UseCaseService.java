package com.jdroid.android.usecase.service;

import android.annotation.SuppressLint;
import android.content.Intent;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.notification.NotificationUtils;
import com.jdroid.android.service.WorkerService;
import com.jdroid.android.usecase.AbstractUseCase;
import com.jdroid.java.concurrent.ExecutorUtils;

@SuppressLint("Registered")
public class UseCaseService extends WorkerService {
	
	private final static String USE_CASE = "useCase";
	private final static String NOTIFICATION_TO_CANCEL_ID = "notificationToCancelId";
	
	@Override
	protected void doExecute(Intent intent) {
		
		int notificationToCancelId = intent.getIntExtra(NOTIFICATION_TO_CANCEL_ID, 0);
		if (notificationToCancelId != 0) {
			NotificationUtils.cancelNotification(notificationToCancelId);
		}
		
		AbstractUseCase useCase = (AbstractUseCase)intent.getSerializableExtra(USE_CASE);
		useCase.run();
	}

	@Override
	protected Boolean timingTrackingEnabled() {
		return false;
	}

	public static void schedule(final AbstractUseCase useCase, Long delaySeconds) {
		ExecutorUtils.schedule(new Runnable() {
			
			@Override
			public void run() {
				execute(useCase);
			}
		}, delaySeconds);
	}
	
	public static void execute(AbstractUseCase useCase) {
		WorkerService.runIntentInService(AbstractApplication.get(), getServiceIntent(useCase, null), UseCaseService.class);
	}
	
	public static Intent getServiceIntent(AbstractUseCase useCase, Integer notificationToCancelId) {
		Intent intent = new Intent();
		intent.putExtra(USE_CASE, useCase);
		intent.putExtra(NOTIFICATION_TO_CANCEL_ID, notificationToCancelId);
		return WorkerService.getServiceIntent(AbstractApplication.get(), intent, UseCaseService.class);
	}
	
}
