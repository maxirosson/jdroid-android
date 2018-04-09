package com.jdroid.android.concurrent;

import com.jdroid.android.application.AbstractApplication;

public abstract class SafeRunnable implements Runnable {
	
	@Override
	public final void run() {
		try {
			doRun();
		} catch (RuntimeException e) {
			AbstractApplication.get().getExceptionHandler().logHandledException(e);
		}
	}
	
	public abstract void doRun();
}
