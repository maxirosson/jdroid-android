package com.jdroid.android.usecase.listener;

import com.jdroid.android.AbstractApplication;

/**
 * Empty Default Use Case Listener
 * 
 * @author Maxi Rosson
 */
public class EmptyDefaultUseCaseListener implements DefaultUseCaseListener {
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onStartUseCase()
	 */
	@Override
	public void onStartUseCase() {
		// Do Nothing
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onUpdateUseCase()
	 */
	@Override
	public void onUpdateUseCase() {
		// Do Nothing
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		// Do Nothing
		
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onFinishFailedUseCase(java.lang.RuntimeException)
	 */
	@Override
	public void onFinishFailedUseCase(RuntimeException runtimeException) {
		AbstractApplication.get().getExceptionHandler().logHandledException(runtimeException);
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onFinishCanceledUseCase()
	 */
	@Override
	public void onFinishCanceledUseCase() {
		// Do Nothing
	}
}
