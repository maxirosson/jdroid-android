package com.jdroid.android.usecase;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.usecase.listener.UseCaseListener;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.exception.AbstractException;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.io.Serializable;
import java.util.List;

public abstract class AbstractUseCase implements Runnable, Serializable {
	
	private static final long serialVersionUID = 3732327346852606739L;
	
	private static final Logger LOGGER = LoggerUtils.getLogger(AbstractUseCase.class);
	
	public enum UseCaseStatus {
		NOT_INVOKED,
		IN_PROGRESS,
		FINISHED_SUCCESSFUL,
		FINISHED_FAILED;
	}
	
	private volatile List<UseCaseListener> listeners = Lists.newArrayList();
	private volatile UseCaseStatus useCaseStatus = UseCaseStatus.NOT_INVOKED;
	private AbstractException abstractException;
	private volatile Boolean notified = false;
	
	private Long executionTime = 0L;
	private int exceptionPriorityLevel = AbstractException.NORMAL_PRIORITY;

	/**
	 * Executes the use case.
	 */
	@Override
	public final void run() {
		
		LOGGER.debug("Executing " + getClass().getSimpleName());
		markAsInProgress();
		for (UseCaseListener listener : listeners) {
			notifyUseCaseStart(listener);
		}
		try {
			
			LOGGER.debug("Started use case " + getClass().getSimpleName());
			long startTime = DateUtils.nowMillis();
			doExecute();
			executionTime = DateUtils.nowMillis() - startTime;
			LOGGER.debug("Finished use case " + getClass().getSimpleName() + ". Execution time: "
					+ DateUtils.formatDuration(executionTime));
			
			markAsSuccessful();
			for (UseCaseListener listener : listeners) {
				notifyFinishedUseCase(listener);
			}
			AbstractApplication.get().getAnalyticsSender().trackUseCaseTiming(getClass(), executionTime);
		} catch (RuntimeException e) {
			AbstractException abstractException;
			if (e instanceof AbstractException) {
				abstractException = (AbstractException)e;
			} else {
				abstractException = new UnexpectedException(e);
			}
			abstractException.setPriorityLevel(exceptionPriorityLevel);
			markAsFailed(abstractException);

			logHandledException(abstractException);

			for (UseCaseListener listener : listeners) {
				notifyFailedUseCase(abstractException, listener);
			}
		} finally {
			if (!listeners.isEmpty()) {
				markAsNotified();
			}
		}
	}

	protected void logHandledException(AbstractException abstractException) {
		AbstractApplication.get().getExceptionHandler().logHandledException(abstractException);
	}
	
	/**
	 * @return the executionTime
	 */
	public Long getExecutionTime() {
		return executionTime;
	}
	
	/**
	 * Override this method with the use case functionality to be executed
	 */
	protected abstract void doExecute();
	
	/**
	 * Notifies the listener that the use case has started. <br/>
	 * You can override this method for a custom notification when your listeners may be listening to multiple use cases
	 * at a time.
	 * 
	 * @param listener The listener to notify.
	 */
	protected void notifyUseCaseStart(UseCaseListener listener) {
		listener.onStartUseCase();
	}
	
	/**
	 * Notifies the listener that the use case has finished successfully. <br/>
	 * You can override this method for a custom notification when your listeners may be listening to multiple use cases
	 * at a time.
	 * 
	 * @param listener The listener to notify.
	 */
	protected void notifyFinishedUseCase(UseCaseListener listener) {
		listener.onFinishUseCase();
	}
	
	/**
	 * Notifies the listener that the use case has failed to execute. <br/>
	 * You can override this method for a custom notification when your listeners may be listening to multiple use cases
	 * at a time.
	 * 
	 * @param listener The listener to notify.
	 */
	protected void notifyFailedUseCase(AbstractException e, UseCaseListener listener) {
		listener.onFinishFailedUseCase(e);
	}
	
	/**
	 * @return the listeners
	 */
	protected List<UseCaseListener> getListeners() {
		return listeners;
	}
	
	/**
	 * @param listener the listener to add
	 */
	public void addListener(UseCaseListener listener) {
		if (listener != null && !listeners.contains(listener)) {
			this.listeners.add(listener);
		}
	}
	
	/**
	 * @param listener the listener to remove
	 */
	public void removeListener(UseCaseListener listener) {
		if (listener != null) {
			this.listeners.remove(listener);
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
	public Boolean isFinish() {
		return (isFinishFailed() || isFinishSuccessful());
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
}
