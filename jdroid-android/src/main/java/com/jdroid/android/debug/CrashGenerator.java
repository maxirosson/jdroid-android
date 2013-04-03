package com.jdroid.android.debug;

import com.jdroid.android.exception.CommonErrorCode;
import com.jdroid.java.exception.ConnectionException;
import com.jdroid.java.utils.ExecutorUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class CrashGenerator {
	
	private static final String CRASH_MESSAGE = "This is a generated crash for testing";
	
	public static void crash(final String crashType, Boolean executeOnNewThread) {
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				if (crashType.startsWith("BusinessException")) {
					throw CommonErrorCode.INTERNAL_ERROR.newBusinessException(CRASH_MESSAGE);
				} else if (crashType.startsWith("ConnectionException")) {
					throw new ConnectionException(null, CRASH_MESSAGE);
				} else if (crashType.startsWith("ApplicationException")) {
					throw CommonErrorCode.SERVER_ERROR.newApplicationException(CRASH_MESSAGE);
				} else if (crashType.startsWith("RuntimeException")) {
					throw new RuntimeException(CRASH_MESSAGE);
				}
			}
		};
		if (executeOnNewThread) {
			ExecutorUtils.execute(runnable);
		} else {
			runnable.run();
		}
	}
	
}
