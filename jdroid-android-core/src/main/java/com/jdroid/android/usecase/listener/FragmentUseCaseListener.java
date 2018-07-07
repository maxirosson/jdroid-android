package com.jdroid.android.usecase.listener;

import com.jdroid.android.exception.ErrorDisplayer;
import com.jdroid.android.fragment.FragmentIf;
import com.jdroid.java.exception.AbstractException;

public abstract class FragmentUseCaseListener implements UseCaseListener {
	
	@Override
	public void onStartUseCase() {
		getFragmentIf().showLoading();
	}
	
	@Override
	public void onUpdateUseCase() {
		getFragmentIf().onUpdateUseCase();
	}
	
	@Override
	public void onFinishFailedUseCase(AbstractException abstractException) {
		getFragmentIf().dismissLoading();
		// TODO This line shouldn't be executed outside the onStart/onStop cycle, to avoid IllegalStateException: Can not perform this action after onSaveInstanceState
		createErrorDisplayer(abstractException).displayError(getFragmentIf().getActivityIf().getActivity(), abstractException);
	}

	protected ErrorDisplayer createErrorDisplayer(AbstractException abstractException) {
		return getFragmentIf().createErrorDisplayer(abstractException);
	}
	
	@Override
	public void onFinishUseCase() {
		getFragmentIf().onFinishUseCase();
	}
	
	protected abstract FragmentIf getFragmentIf();
}
