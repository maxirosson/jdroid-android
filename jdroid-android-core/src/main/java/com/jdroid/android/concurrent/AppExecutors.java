package com.jdroid.android.concurrent;

import android.os.Handler;
import android.os.Looper;

import com.jdroid.java.concurrent.NormalPriorityThreadFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Global executor pools for the whole application.
 *
 * Grouping tasks like this avoids the effects of task starvation
 * (e.g. disk reads don't wait behind webservice requests).
 */
public class AppExecutors {

	private static Executor diskIO = Executors.newSingleThreadExecutor(new NormalPriorityThreadFactory("jdroid-disk"));
	private static Executor networkIO = Executors.newFixedThreadPool(3, new NormalPriorityThreadFactory("jdroid-network"));
	private static Executor mainThread = new MainThreadExecutor();

	public static Executor getDiskIOExecutor() {
		return diskIO;
	}

	public static Executor getNetworkIOExecutor() {
		return networkIO;
	}

	public static Executor getMainThreadExecutor() {
		return mainThread;
	}

	private static class MainThreadExecutor implements Executor {

		private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

		@Override
		public void execute(Runnable command) {
			mainThreadHandler.post(command);
		}
	}
}
