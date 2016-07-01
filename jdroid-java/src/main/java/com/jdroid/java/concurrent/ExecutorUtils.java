package com.jdroid.java.concurrent;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import com.jdroid.java.utils.LoggerUtils;

public final class ExecutorUtils {

	private ExecutorUtils(){}
	
	private static final Logger LOGGER = LoggerUtils.getLogger(ExecutorUtils.class);
	
	// Default amount of thread inside the pool
	private static final int DEFAULT_THREAD_POOL_SIZE = 5;
	
	private static final Executor fixedExecutor = Executors.newCachedThreadPool(new NormalPriorityThreadFactory());
	
	private static final Executor fixedLowPriorityExecutor = Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE,
		new LowPriorityThreadFactory());
	
	private static final ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(1,
		new NormalPriorityThreadFactory("schedule"));
	
	/**
	 * @param runnable The {@link Runnable} task
	 */
	public static void execute(Runnable runnable) {
		fixedExecutor.execute(runnable);
	}
	
	public static void executeWithLowPriority(Runnable runnable) {
		fixedLowPriorityExecutor.execute(runnable);
	}
	
	public static void schedule(Runnable runnable, Long delaySeconds) {
		scheduledExecutor.schedule(runnable, delaySeconds, TimeUnit.SECONDS);
	}
	
	public static ScheduledFuture<?> schedule(Runnable runnable, Long delaySeconds, Long period) {
		return scheduledExecutor.scheduleAtFixedRate(runnable, delaySeconds, period, TimeUnit.SECONDS);
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
			LoggerUtils.logHandledException(LOGGER, e);
		}
	}
	
	/**
	 * @param millis The time to sleep in milliseconds.
	 */
	public static void sleepInMillis(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			LoggerUtils.logHandledException(LOGGER, e);
		}
	}
}