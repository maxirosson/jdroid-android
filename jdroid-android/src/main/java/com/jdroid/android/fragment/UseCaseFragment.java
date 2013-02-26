package com.jdroid.android.fragment;

import android.os.Bundle;
import com.jdroid.android.fragment.BaseFragment.UseCaseTrigger;
import com.jdroid.android.usecase.DefaultAbstractUseCase;

/**
 * 
 * @param <T>
 * @author Maxi Rosson
 */
public abstract class UseCaseFragment<T extends DefaultAbstractUseCase> extends AbstractFragment {
	
	private T useCase;
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
		useCase = getInstance(getUseCaseClass());
		intializeUseCase(useCase);
	}
	
	protected void intializeUseCase(T useCase) {
		// Do Nothing
	}
	
	public T getUseCase() {
		return useCase;
	}
	
	protected abstract Class<T> getUseCaseClass();
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		onResumeUseCase(useCase, this, getUseCaseTrigger());
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		onPauseUseCase(useCase, this);
	}
	
	protected UseCaseTrigger getUseCaseTrigger() {
		return UseCaseTrigger.ONCE;
	}
	
	public void executeUseCase() {
		executeUseCase(useCase);
	}
}
