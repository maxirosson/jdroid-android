package com.jdroid.android.glide;

import android.support.annotation.Nullable;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.HttpException;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.jdroid.android.application.AbstractApplication;

public class LoggingRequestListener<R> implements RequestListener<R> {

	@Override
	public boolean onLoadFailed(@Nullable GlideException glideException, Object model, Target target, boolean isFirstResource) {
		if (glideException != null) {
			Throwable mainThrowable = null;
			for (Throwable throwable : glideException.getRootCauses()) {
				if (throwable instanceof OutOfMemoryError || throwable instanceof HttpException) {
					mainThrowable = throwable;
					break;
				}
			}
			if (mainThrowable == null) {
				mainThrowable = glideException.getRootCauses().get(0);
			}
			AbstractApplication.get().getCoreAnalyticsSender().trackErrorLog("Glide failed to load " + model);
			AbstractApplication.get().getExceptionHandler().logHandledException(mainThrowable);
		}
		return false;
	}

	@Override
	public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
		return false;
	}
}
