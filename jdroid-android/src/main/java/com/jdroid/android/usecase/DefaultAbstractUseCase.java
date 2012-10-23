package com.jdroid.android.usecase;

import com.jdroid.android.usecase.listener.DefaultUseCaseListener;

/**
 * Abstract use case that handles the calls to {@link DefaultUseCaseListener#onStartUseCase()},
 * {@link DefaultUseCaseListener#onFinishUseCase()}, {@link DefaultUseCaseListener#onFinishCanceledUseCase()} and
 * {@link DefaultUseCaseListener#onFinishFailedUseCase(RuntimeException)} when executing.
 * 
 * @author Maxi Rosson
 */
public abstract class DefaultAbstractUseCase extends AbstractUseCase<DefaultUseCaseListener> {
	
	/**
	 * @see com.jdroid.android.usecase.AbstractUseCase#notifyFailedUseCase(java.lang.RuntimeException, java.lang.Object)
	 */
	@Override
	protected void notifyFailedUseCase(RuntimeException e, DefaultUseCaseListener listener) {
		listener.onFinishFailedUseCase(e);
	}
	
	/**
	 * @see com.jdroid.android.usecase.AbstractUseCase#notifyFinishedUseCase(java.lang.Object)
	 */
	@Override
	protected void notifyFinishedUseCase(DefaultUseCaseListener listener) {
		listener.onFinishUseCase();
	}
	
	/**
	 * @see com.jdroid.android.usecase.AbstractUseCase#notifyFinishedCanceledUseCase(java.lang.Object)
	 */
	@Override
	protected void notifyFinishedCanceledUseCase(DefaultUseCaseListener listener) {
		listener.onFinishCanceledUseCase();
	}
	
	/**
	 * @see com.jdroid.android.usecase.AbstractUseCase#notifyUseCaseStart(java.lang.Object)
	 */
	@Override
	protected void notifyUseCaseStart(DefaultUseCaseListener listener) {
		listener.onStartUseCase();
	}
}
