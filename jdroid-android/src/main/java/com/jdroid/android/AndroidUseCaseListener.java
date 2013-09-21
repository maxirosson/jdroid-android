package com.jdroid.android;

import com.jdroid.android.activity.ActivityIf;
import com.jdroid.android.exception.DefaultExceptionHandler;
import com.jdroid.android.usecase.listener.DefaultUseCaseListener;

/**
 * 
 * @author Maxi Rosson
 */
public abstract class AndroidUseCaseListener implements DefaultUseCaseListener {
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onStartUseCase()
	 */
	@Override
	public void onStartUseCase() {
		getActivityIf().onStartUseCase();
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onUpdateUseCase()
	 */
	@Override
	public void onUpdateUseCase() {
		getActivityIf().onUpdateUseCase();
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onFinishFailedUseCase(java.lang.RuntimeException)
	 */
	@Override
	public void onFinishFailedUseCase(RuntimeException runtimeException) {
		if (goBackOnError(runtimeException)) {
			DefaultExceptionHandler.markAsGoBackOnError(runtimeException);
		} else {
			DefaultExceptionHandler.markAsNotGoBackOnError(runtimeException);
		}
		ActivityIf activityIf = getActivityIf();
		if (activityIf != null) {
			activityIf.dismissLoadingOnUIThread();
		}
		
		throw runtimeException;
	}
	
	public Boolean goBackOnError(RuntimeException runtimeException) {
		ActivityIf activityIf = getActivityIf();
		if (activityIf != null) {
			return activityIf.goBackOnError(runtimeException);
		} else {
			return true;
		}
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		getActivityIf().onFinishUseCase();
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onFinishCanceledUseCase()
	 */
	@Override
	public void onFinishCanceledUseCase() {
		getActivityIf().onFinishCanceledUseCase();
	}
	
	protected abstract ActivityIf getActivityIf();
}
