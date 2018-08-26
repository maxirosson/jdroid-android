package com.jdroid.android.glide;

import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.jdroid.android.application.AbstractApplication;

public class LoggingUncaughtThrowableStrategy implements GlideExecutor.UncaughtThrowableStrategy {

	@Override
	public void handle(Throwable t) {
		GlideExecutor.UncaughtThrowableStrategy.LOG.handle(t);
		AbstractApplication.get().getExceptionHandler().logHandledException(t);
	}
}
