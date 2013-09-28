package com.jdroid.android.service;

import android.content.Intent;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.usecase.DefaultAbstractUseCase;
import com.jdroid.java.utils.ExecutorUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class UseCaseService extends WorkerService {
	
	private final static String USE_CASE = "useCase";
	
	/**
	 * @see com.despegar.commons.service.WorkerService#doExecute(android.content.Intent)
	 */
	@Override
	protected void doExecute(Intent intent) {
		
		DefaultAbstractUseCase useCase = (DefaultAbstractUseCase)intent.getExtras().get(USE_CASE);
		useCase.run();
		
		if (useCase.isFinishFailed()) {
			AbstractApplication.get().getExceptionHandler().logHandledException(useCase.getRuntimeException());
		}
	}
	
	public static void schedule(final DefaultAbstractUseCase useCase, Long delaySeconds) {
		ExecutorUtils.schedule(new Runnable() {
			
			@Override
			public void run() {
				execute(useCase);
			}
		}, delaySeconds);
	}
	
	public static void execute(DefaultAbstractUseCase useCase) {
		Intent intent = new Intent();
		intent.putExtra(USE_CASE, useCase);
		WorkerService.runIntentInService(AbstractApplication.get(), intent, UseCaseService.class, false);
	}
	
}
