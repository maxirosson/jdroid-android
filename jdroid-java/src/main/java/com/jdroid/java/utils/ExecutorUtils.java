package com.jdroid.java.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;

public final class ExecutorUtils {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(ExecutorUtils.class);
	
	// Default amount of thread inside the pool
	private static final int DEFAULT_THREAD_POOL_SIZE = 10;
	
	private static final Executor fixedExecutor = Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);
	
	private static final ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(1);
	
	/**
	 * @param runnable The {@link Runnable} task
	 */
	public static void execute(Runnable runnable) {
		fixedExecutor.execute(runnable);
	}
	
	public static void schedule(Runnable runnable, Long delaySeconds) {
		scheduledExecutor.schedule(runnable, delaySeconds, TimeUnit.SECONDS);
	}
	
	public static void scheduleInMillis(Runnable runnable, Long delayMilliSeconds) {
		scheduledExecutor.schedule(runnable, delayMilliSeconds, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * @param seconds The time to sleep in seconds.
	 */
	public static void sleep(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			LOGGER.error("Error when sleeping", e);
		}
	}
	
	/**
	 * @param millis The time to sleep in milliseconds.
	 */
	public static void sleepInMillis(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			LOGGER.error("Error when sleeping", e);
		}
	}
}