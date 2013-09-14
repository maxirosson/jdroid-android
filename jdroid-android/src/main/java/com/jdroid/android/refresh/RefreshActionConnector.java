package com.jdroid.android.refresh;

import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.jdroid.android.R;
import com.jdroid.android.activity.ActivityIf;
import com.jdroid.android.exception.DefaultExceptionHandler;
import com.jdroid.android.usecase.listener.DefaultUseCaseListener;

/**
 * 
 * @author Maxi Rosson
 */
public abstract class RefreshActionConnector implements RefreshActionProvider.OnRefreshListener, DefaultUseCaseListener {
	
	private RefreshActionProvider refreshActionProvider;
	
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		MenuItem menuItem = menu.findItem(R.id.refresh_action_item);
		refreshActionProvider = (RefreshActionProvider)MenuItemCompat.getActionProvider(menuItem);
		refreshActionProvider.setTitle(menuItem.getTitle());
		refreshActionProvider.setOnRefreshListener(this);
	}
	
	public void startLoading() {
		if (refreshActionProvider != null) {
			refreshActionProvider.startLoading();
		}
	}
	
	public void startLoadingOnUIThread() {
		getActivityIf().executeOnUIThread(new Runnable() {
			
			@Override
			public void run() {
				startLoading();
			}
		});
	}
	
	public void stopLoading() {
		if (refreshActionProvider != null) {
			refreshActionProvider.stopLoading();
		}
	}
	
	public void stopLoadingOnUIThread() {
		getActivityIf().executeOnUIThread(new Runnable() {
			
			@Override
			public void run() {
				stopLoading();
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onStartUseCase()
	 */
	@Override
	public void onStartUseCase() {
		startLoadingOnUIThread();
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
		stopLoadingOnUIThread();
		throw runtimeException;
	}
	
	public Boolean goBackOnError(RuntimeException runtimeException) {
		return getActivityIf().goBackOnError(runtimeException);
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		stopLoadingOnUIThread();
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
