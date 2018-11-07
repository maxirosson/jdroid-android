package com.jdroid.android.debug.crash;

import com.jdroid.android.concurrent.AppExecutors;
import com.jdroid.android.exception.DialogErrorDisplayer;
import com.jdroid.java.exception.AbstractException;

public class CrashGenerator {

	public static void crash(final ExceptionType exceptionType, Boolean executeOnNewThread) {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				try {
					exceptionType.crash();
				} catch (AbstractException e) {
					DialogErrorDisplayer.markAsNotGoBackOnError(e);
					throw e;
				}
			}
		};
		if (executeOnNewThread) {
			AppExecutors.getNetworkIOExecutor().execute(runnable);
		} else {
			runnable.run();
		}
	}
}
