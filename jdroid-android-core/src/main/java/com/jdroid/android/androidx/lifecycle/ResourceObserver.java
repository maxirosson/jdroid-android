package com.jdroid.android.androidx.lifecycle;

import com.jdroid.android.exception.AbstractErrorDisplayer;
import com.jdroid.android.exception.DialogErrorDisplayer;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.java.exception.AbstractException;
import com.jdroid.java.utils.LoggerUtils;

import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

public abstract class ResourceObserver<T> implements Observer<Resource<T>> {

	@MainThread
	@Override
	public void onChanged(Resource<T> resource) {
		if (resource != null) {
			LoggerUtils.getLogger(getTag()).info("ResourceObserver notifying " + resource);
			if (resource.getStatus().equals(Resource.Status.STARTING)) {
				onStarting();
			} else if (resource.getStatus().equals(Resource.Status.LOADING)) {
				onStartLoading(resource.getData());
				if (resource.getData() != null) {
					onDataChanged(resource.getData());
				}
			} else if (resource.getStatus().equals(Resource.Status.SUCCESS)) {
				onDataChanged(resource.getData());
				onStopLoading(resource.getData());
			} else if (resource.getStatus().equals(Resource.Status.ERROR)) {
				onStopLoading(resource.getData());
				onError(resource.getException(), resource.getData());
			}
		} else {
			LoggerUtils.getLogger(getTag()).warn("ResourceObserver received null resource");
		}
	}

	@MainThread
	protected void onStarting() {
		// Do nothing
	}

	@MainThread
	protected void onStartLoading(@Nullable T data) {
		if (data == null) {
			getFragment().showLoading();
		}
	}

	@MainThread
	protected void onStopLoading(@Nullable T data) {
		getFragment().dismissLoading();
	}

	@MainThread
	protected abstract void onDataChanged(T data);

	@MainThread
	protected void onError(AbstractException exception, @Nullable T data) {
		DialogErrorDisplayer.markAsNotGoBackOnError(exception);
		AbstractErrorDisplayer.getErrorDisplayer(exception).displayError(getFragment().getActivity(), exception);
	}

	protected abstract AbstractFragment getFragment();

	protected String getTag() {
		Fragment fragment = getFragment();
		if (fragment != null) {
			return fragment.getClass().getSimpleName();
		} else {
			return getClass().getSimpleName();
		}
	}

}
