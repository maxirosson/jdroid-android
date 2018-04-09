package com.jdroid.android.usecase.listener;

/**
 * Use Case Listener that doesn't display a loading dialog
 */
public abstract class LoadinglessUseCaseListener extends FragmentUseCaseListener {
	
	@Override
	public void onStartUseCase() {
		// Do Nothing
	}
	
	@Override
	public void onUpdateUseCase() {
		// Do Nothing
	}
	
	@Override
	public void onFinishUseCase() {
		// Do Nothing
	}
}
