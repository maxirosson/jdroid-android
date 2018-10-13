package com.jdroid.android.refresh;

import androidx.core.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.jdroid.android.exception.AbstractErrorDisplayer;
import com.jdroid.android.exception.ErrorDisplayer;
import com.jdroid.android.fragment.FragmentIf;
import com.jdroid.android.usecase.listener.UseCaseListener;
import com.jdroid.java.exception.AbstractException;

public abstract class RefreshActionConnector implements RefreshActionProvider.OnRefreshListener, UseCaseListener {

	private RefreshActionProvider refreshActionProvider;

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		MenuItem menuItem = menu.findItem(getMenuItemResId());
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
		getFragmentIf().executeOnUIThread(new Runnable() {

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
		getFragmentIf().executeOnUIThread(new Runnable() {

			@Override
			public void run() {
				stopLoading();
			}
		});
	}

	@Override
	public void onStartUseCase() {
		startLoadingOnUIThread();
	}

	@Override
	public void onUpdateUseCase() {
		getFragmentIf().onUpdateUseCase();
	}

	@Override
	public void onFinishFailedUseCase(AbstractException abstractException) {
		stopLoading();
		createErrorDisplayer(abstractException).displayError(getFragmentIf().getActivityIf().getActivity(), abstractException);
	}

	public ErrorDisplayer createErrorDisplayer(AbstractException abstractException) {
		return AbstractErrorDisplayer.getErrorDisplayer(abstractException);
	}

	@Override
	public void onFinishUseCase() {
		stopLoadingOnUIThread();
	}

	protected abstract FragmentIf getFragmentIf();

	protected abstract int getMenuItemResId();
}
