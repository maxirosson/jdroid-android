package com.jdroid.android.usecase;

import java.io.Serializable;
import java.util.List;
import org.slf4j.Logger;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.DateUtils;
import com.jdroid.java.utils.ExecutorUtils;
import com.jdroid.java.utils.LoggerUtils;

/**
 * 
 * @param <T> The Listener type
 * 
 * @author Maxi Rosson
 */
public abstract class AbstractUseCase<T> implements UseCase<T>, Serializable {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(AbstractUseCase.class);
	
	public enum UseCaseStatus {
		NOT_INVOKED,
		IN_PROGRESS,
		FINISHED_SUCCESSFUL,
		FINISHED_FAILED,
		CANCELED;
	}
	
	private List<T> listeners = Lists.newArrayList();
	private UseCaseStatus useCaseStatus = UseCaseStatus.NOT_INVOKED;
	private RuntimeException runtimeException;
	private Boolean notified = false;
	
	private Long executionTime = 0L;
	private Long minimumExecutionTime = 0L;
	
	/**
	 * Executes the use case.
	 */
	@Override
	public final void run() {
		
		LOGGER.debug("Executing " + getClass().getSimpleName());
		markAsInProgress();
		for (T listener : listeners) {
			notifyUseCaseStart(listener);
		}
		try {
			
			LOGGER.debug("Started use case " + getClass().getSimpleName());
			long startTime = System.currentTimeMillis();
			doExecute();
			executionTime = System.currentTimeMillis() - startTime;
			LOGGER.debug("Finished use case. Execution time: " + DateUtils.formatSecondsAndMilli(executionTime));
			
			if (executionTime < minimumExecutionTime) {
				ExecutorUtils.sleepInMillis(minimumExecutionTime - executionTime);
				LOGGER.debug("Delaying the use case to achieve the minimum execution time of " + minimumExecutionTime
						+ "ms");
			}
			
			if (isCanceled()) {
				for (T listener : listeners) {
					notifyFinishedCanceledUseCase(listener);
				}
			} else {
				for (T listener : listeners) {
					notifyFinishedUseCase(listener);
				}
				markAsSuccessful();
			}
		} catch (RuntimeException e) {
			markAsFailed(e);
			for (T listener : listeners) {
				notifyFailedUseCase(e, listener);
			}
		} finally {
			if (!listeners.isEmpty()) {
				markAsNotified();
			}
		}
	}
	
	/**
	 * @param minimumExecutionTime the minimumExecutionTime to set
	 */
	public void setMinimumExecutionTime(Long minimumExecutionTime) {
		this.minimumExecutionTime = minimumExecutionTime;
	}
	
	/**
	 * @return the executionTime
	 */
	public Long getExecutionTime() {
		return executionTime;
	}
	
	/**
	 * Override this method with the functionality to be executed between
	 * {@link NoResultUseCaseListener#onStartUseCase()} and {@link NoResultUseCaseListener#onFinishUseCase()}
	 */
	protected abstract void doExecute();
	
	/**
	 * Notifies the listener that the use case has started. <br/>
	 * You can override this method for a custom notification when your listeners may be listening to multiple use cases
	 * at a time.
	 * 
	 * @param listener The listener to notify.
	 */
	protected abstract void notifyUseCaseStart(T listener);
	
	/**
	 * Notifies the listener that a canceled use case has finished. <br/>
	 * You can override this method for a custom notification when your listeners may be listening to multiple use cases
	 * at a time.
	 * 
	 * @param listener The listener to notify.
	 */
	protected abstract void notifyFinishedCanceledUseCase(T listener);
	
	/**
	 * Notifies the listener that the use case has finished successfully. <br/>
	 * You can override this method for a custom notification when your listeners may be listening to multiple use cases
	 * at a time.
	 * 
	 * @param listener The listener to notify.
	 */
	protected abstract void notifyFinishedUseCase(T listener);
	
	/**
	 * Notifies the listener that the use case has failed to execute. <br/>
	 * You can override this method for a custom notification when your listeners may be listening to multiple use cases
	 * at a time.
	 * 
	 * @param listener The listener to notify.
	 */
	protected abstract void notifyFailedUseCase(RuntimeException e, T listener);
	
	/**
	 * @return the listeners
	 */
	protected List<T> getListeners() {
		return listeners;
	}
	
	/**
	 * @param listener the listener to add
	 */
	@Override
	public void addListener(T listener) {
		if (!listeners.contains(listener)) {
			this.listeners.add(listener);
		}
	}
	
	/**
	 * @param listener the listener to remove
	 */
	@Override
	public void removeListener(T listener) {
		this.listeners.remove(listener);
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
	
	public Boolean isCanceled() {
		return UseCaseStatus.CANCELED.equals(useCaseStatus);
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
		runtimeException = null;
		this.useCaseStatus = UseCaseStatus.IN_PROGRESS;
	}
	
	protected void markAsSuccessful() {
		this.useCaseStatus = UseCaseStatus.FINISHED_SUCCESSFUL;
	}
	
	protected void markAsFailed(RuntimeException runtimeException) {
		this.useCaseStatus = UseCaseStatus.FINISHED_FAILED;
		this.runtimeException = runtimeException;
	}
	
	public void cancel() {
		this.useCaseStatus = UseCaseStatus.CANCELED;
	}
	
	public RuntimeException getRuntimeException() {
		return runtimeException;
	}
	
	public Boolean isNotified() {
		return notified;
	}
}
