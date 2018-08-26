package com.jdroid.android.listener;

import android.view.View;
import android.view.View.OnClickListener;

import com.jdroid.android.usecase.AbstractUseCase;
import com.jdroid.java.concurrent.ExecutorUtils;

/**
 * {@link OnClickListener} that execute a {@link AbstractUseCase}
 */
public class UseCaseOnClickListener implements OnClickListener {

	private AbstractUseCase useCase;

	/**
	 * @param useCase The {@link AbstractUseCase} to execute
	 */
	public UseCaseOnClickListener(AbstractUseCase useCase) {
		this.useCase = useCase;
	}

	/**
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public final void onClick(final View view) {
		ExecutorUtils.execute(useCase);
	}

}
