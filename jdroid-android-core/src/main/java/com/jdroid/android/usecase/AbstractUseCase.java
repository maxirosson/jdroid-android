package com.jdroid.android.usecase;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.WorkerThread;

import com.google.firebase.perf.metrics.Trace;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.firebase.performance.TraceHelper;
import com.jdroid.android.usecase.listener.UseCaseListener;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.exception.AbstractException;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.io.Serializable;
import java.util.List;

import static android.support.annotation.RestrictTo.Scope.LIBRARY;

public abstract class AbstractUseCase implements Runnable, Serializable {
	
	private static final long serialVersionUID = 3732327346852606739L;
	
	private final static Logger LOGGER = LoggerUtils.getLogger(AbstractUseCase.class);

	public enum UseCaseStatus {
		NOT_INVOKED,
		IN_PROGRESS,
		FINISHED_SUCCESSFUL,
		FINISHED_FAILED;
	}
	
	private transient volatile List<UseCaseListener> listeners;
	private transient Handler handler;

	private volatile UseCaseStatus useCaseStatus = UseCaseStatus.NOT_INVOKED;
	private AbstractException abstractException;
	private volatile Boolean notified = false;
	
	private Long startTime;
	private Long executionTime;
	private int exceptionPriorityLevel = AbstractException.NORMAL_PRIORITY;

	/**
	 * Executes the use case.
	 */
	@Override
	public final void run() {
		
		LOGGER.debug("Executing " + getClass().getSimpleName());
		markAsInProgress();

		if (!Lists.isNullOrEmpty(listeners)) {
			Runnable startUseCaseRunnable = new Runnable() {
				@Override
				public void run() {
					for (UseCaseListener listener : listeners) {
						notifyUseCaseStart(listener);
					}
				}
			};
			if (handler != null) {
				handler.post(startUseCaseRunnable);
			} else {
				startUseCaseRunnable.run();
			}
		}
		
		Trace trace = null;
		if (timingTrackingEnabled()) {
			trace = TraceHelper.startTrace(getClass());
		}

		try {
			
			LOGGER.debug("Started " + getClass().getSimpleName());
			executionTime = null;
			startTime = DateUtils.nowMillis();
			doExecute();
			executionTime = DateUtils.nowMillis() - startTime;
			LOGGER.debug("Finished " + getClass().getSimpleName() + ". Execution time: " + DateUtils.formatDuration(executionTime));
			
			if (trace != null) {
				trace.incrementCounter("success");
			}
			
			markAsSuccessful();

			if (!Lists.isNullOrEmpty(listeners)) {
				Runnable finishedUseCaseRunnable = new Runnable() {
					@Override
					public void run() {
						for (UseCaseListener listener : listeners) {
							notifyFinishedUseCase(listener);
						}
						markAsNotified();
					}
				};
				if (handler != null) {
					handler.post(finishedUseCaseRunnable);
				} else {
					finishedUseCaseRunnable.run();
				}
			}

		} catch (RuntimeException e) {
			if (trace != null) {
				trace.incrementCounter("failure");
			}
			final AbstractException abstractException = wrapException(e);
			markAsFailed(abstractException);
			logHandledException(abstractException);

			if (!Lists.isNullOrEmpty(listeners)) {
				Runnable finishedFailedUseCaseRunnable = new Runnable() {
					@Override
					public void run() {
						for (UseCaseListener listener : listeners) {
							notifyFailedUseCase(abstractException, listener);
						}
						markAsNotified();
					}
				};
				if (handler != null) {
					handler.post(finishedFailedUseCaseRunnable);
				} else {
					finishedFailedUseCaseRunnable.run();
				}
			}
		} finally {
			if (trace != null) {
				trace.stop();
			}
		}
	}
	
	protected Boolean timingTrackingEnabled() {
		return true;
	}

	private AbstractException wrapException(Exception e) {
		final AbstractException abstractException;
		if (e instanceof AbstractException) {
			abstractException = (AbstractException)e;
		} else {
			abstractException = new UnexpectedException(e);
		}
		abstractException.setPriorityLevel(exceptionPriorityLevel);
		return abstractException;
	}

	protected void logHandledException(AbstractException abstractException) {
		AbstractApplication.get().getExceptionHandler().logHandledException(abstractException);
	}
	
	public Long getStartTime() {
		return startTime;
	}
	
	/**
	 * @return the executionTime
	 */
	@Nullable
	public Long getExecutionTime() {
		return executionTime;
	}
	
	/**
	 * Override this method with the use case functionality to be executed
	 */
	@WorkerThread
	protected abstract void doExecute();
	
	/**
	 * Notifies the listener that the use case has started. <br/>
	 * You can override this method for a custom notification when your listeners may be listening to multiple use cases
	 * at a time.
	 * 
	 * @param listener The listener to notify.
	 */
	@RestrictTo(LIBRARY)
	public void notifyUseCaseStart(UseCaseListener listener) {
		try {
			LOGGER.debug("Notifying " + getClass().getSimpleName() + " start to listener " + listener.getClass().getSimpleName());
			listener.onStartUseCase();
		} catch (Exception e) {
			AbstractException abstractException = wrapException(e);
			logHandledException(abstractException);
		}
	}
	
	/**
	 * Notifies the listener that the use case has finished successfully. <br/>
	 * You can override this method for a custom notification when your listeners may be listening to multiple use cases
	 * at a time.
	 * 
	 * @param listener The listener to notify.
	 */
	@RestrictTo(LIBRARY)
	public void notifyFinishedUseCase(UseCaseListener listener) {
		try {
			LOGGER.debug("Notifying " + getClass().getSimpleName() + " finish to listener " + listener.getClass().getSimpleName());
			listener.onFinishUseCase();
		} catch (Exception e) {
			AbstractException abstractException = wrapException(e);
			logHandledException(abstractException);
		}
	}
	
	/**
	 * Notifies the listener that the use case has failed to execute. <br/>
	 * You can override this method for a custom notification when your listeners may be listening to multiple use cases
	 * at a time.
	 * 
	 * @param listener The listener to notify.
	 */
	@RestrictTo(LIBRARY)
	public void notifyFailedUseCase(AbstractException exception, UseCaseListener listener) {
		try {
			LOGGER.debug("Notifying " + getClass().getSimpleName() + " finish failed to listener " + listener.getClass().getSimpleName());
			listener.onFinishFailedUseCase(exception);
		} catch (Exception e) {
			AbstractException abstractException = wrapException(e);
			logHandledException(abstractException);
		}
	}
	
	/**
	 * @return the listeners
	 */
	@Nullable
	protected List<UseCaseListener> getListeners() {
		return listeners;
	}
	
	/**
	 * @param listener the listener to add
	 */
	public void addListener(UseCaseListener listener) {
		if (listener != null) {
			if (listeners == null) {
				listeners = Lists.newArrayList();
			}
			if (!listeners.contains(listener)) {
				this.listeners.add(listener);
			}
		}
	}
	
	/**
	 * @param listener the listener to remove
	 */
	public void removeListener(UseCaseListener listener) {
		if (listener != null && listeners != null) {
			listeners.remove(listener);
		}
	}
	
	public Boolean isNotInvoked() {
		return UseCaseStatus.NOT_INVOKED.equals(useCaseStatus);
	}
	
	public Boolean isInProgress() {
		return UseCaseStatus.IN_PROGRESS.equals(useCaseStatus);
	}
	
	public Boolean isFinishSuccessful() {
		return UseCaseStatus.FINISHED_SUCCESSFUL.equals(useCaseStatus);
	}
	
	public Boolean isFinishFailed() {
		return UseCaseStatus.FINISHED_FAILED.equals(useCaseStatus);
	}
	
	/**
	 * @return If the use case has finished, regardless of the success or failure of its execution.
	 */
	public Boolean isFinished() {
		return isFinishFailed() || isFinishSuccessful();
	}
	
	public void markAsNotified() {
		notified = true;
	}
	
	public void markAsNotNotified() {
		notified = false;
	}
	
	public void markAsNotInvoked() {
		this.useCaseStatus = UseCaseStatus.NOT_INVOKED;
	}
	
	protected void markAsInProgress() {
		notified = false;
		abstractException = null;
		this.useCaseStatus = UseCaseStatus.IN_PROGRESS;
	}
	
	protected void markAsSuccessful() {
		this.useCaseStatus = UseCaseStatus.FINISHED_SUCCESSFUL;
	}
	
	protected void markAsFailed(AbstractException abstractException) {
		this.useCaseStatus = UseCaseStatus.FINISHED_FAILED;
		this.abstractException = abstractException;
	}
	
	public AbstractException getAbstractException() {
		return abstractException;
	}
	
	public Boolean isNotified() {
		return notified;
	}

	public void setExceptionPriorityLevel(int exceptionPriorityLevel) {
		this.exceptionPriorityLevel = exceptionPriorityLevel;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}
}
