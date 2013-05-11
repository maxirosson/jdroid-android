package com.jdroid.android.listener;

import android.view.View;
import android.view.View.OnClickListener;
import com.jdroid.android.usecase.UseCase;
import com.jdroid.java.utils.ExecutorUtils;

/**
 * {@link OnClickListener} that execute a {@link UseCase}
 * 
 * @author Maxi Rosson
 */
public class UseCaseOnClickListener implements OnClickListener {
	
	private UseCase<?> defaultUseCase;
	
	/**
	 * @param defaultUseCase The {@link UseCase} to execute
	 */
	public UseCaseOnClickListener(UseCase<?> defaultUseCase) {
		this.defaultUseCase = defaultUseCase;
	}
	
	/**
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public final void onClick(final View view) {
		ExecutorUtils.execute(defaultUseCase);
	}
	
}
