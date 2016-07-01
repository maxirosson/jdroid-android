package com.jdroid.android.debug;

import com.jdroid.android.exception.DialogErrorDisplayer;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.exception.AbstractException;

public class CrashGenerator {
	
	public static void crash(final ExceptionType exceptionType, Boolean executeOnNewThread) {
		Runnable runnable = () -> {
            try {
                exceptionType.crash();
            } catch (AbstractException e) {
                DialogErrorDisplayer.markAsNotGoBackOnError(e);
                throw e;
            }
        };
		if (executeOnNewThread) {
			ExecutorUtils.execute(runnable);
		} else {
			runnable.run();
		}
	}
}
