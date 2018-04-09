package com.jdroid.android.usecase.listener;

import com.jdroid.java.exception.AbstractException;

/**
 * Default Use Case Listener
 */
public interface UseCaseListener {
	
	/**
	 * Called before the use case starts
	 */
	public void onStartUseCase();
	
	/**
	 * Called after the use case starts to report an update status if necessary
	 */
	public void onUpdateUseCase();
	
	/**
	 * Called after the use case fails
	 * 
	 * @param abstractException The {@link AbstractException} with the error
	 */
	public void onFinishFailedUseCase(AbstractException abstractException);
	
	/**
	 * Called when the use case finishes successfully
	 */
	public void onFinishUseCase();
	
}
