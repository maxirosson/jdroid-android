package com.jdroid.android.usecase.listener;

import com.jdroid.android.exception.ErrorDisplayer;
import com.jdroid.android.fragment.FragmentIf;
import com.jdroid.java.exception.AbstractException;

public abstract class ActivityLoadingUseCaseListener implements UseCaseListener {
	
	@Override
	public void onStartUseCase() {
		getFragmentIf().getActivityIf().showLoading();
	}
	
	@Override
	public void onUpdateUseCase() {
		getFragmentIf().onUpdateUseCase();
	}
	
	@Override
	public void onFinishFailedUseCase(AbstractException abstractException) {
		getFragmentIf().getActivityIf().dismissLoading();
		// TODO This line shouldn't be executed outside the onStart/onStop cycle, to avoid IllegalStateException: Can not perform this action after onSaveInstanceState
		createErrorDisplayer(abstractException).displayError(abstractException);
	}

	protected ErrorDisplayer createErrorDisplayer(AbstractException abstractException) {
		return getFragmentIf().createErrorDisplayer(abstractException);
	}

	@Override
	public void onFinishUseCase() {
		getFragmentIf().getActivityIf().dismissLoading();
	}
	
	protected abstract FragmentIf getFragmentIf();
}
