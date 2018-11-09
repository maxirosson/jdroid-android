package com.jdroid.android.androidx.lifecycle;

import com.jdroid.android.exception.AbstractErrorDisplayer;
import com.jdroid.android.exception.DialogErrorDisplayer;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.java.exception.AbstractException;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

public abstract class ResourceObserver<T> implements Observer<Resource<T>> {

	@Override
	public void onChanged(Resource<T> resource) {
		if (resource != null) {
			if (resource.getStatus().equals(Resource.Status.LOADING)) {
				if (resource.getData() != null) {
					showLoading(resource.getData());
				}
			} else if (resource.getStatus().equals(Resource.Status.SUCCESS)) {
				onSuccess(resource.getData());
				dismissLoading(resource.getData());
			} else if (resource.getStatus().equals(Resource.Status.ERROR)) {
				dismissLoading(resource.getData());
				onError(resource.getException(), resource.getData());
			}
		}
	}

	protected void showLoading(@Nullable T data) {
		getFragment().showLoading();
	}

	protected void dismissLoading(@Nullable T data) {
		getFragment().dismissLoading();
	}

	protected abstract void onSuccess(T data);

	protected void onError(AbstractException exception, @Nullable T data) {
		DialogErrorDisplayer.markAsNotGoBackOnError(exception);
		AbstractErrorDisplayer.getErrorDisplayer(exception).displayError(getFragment().getActivity(), exception);
	}

	protected abstract AbstractFragment getFragment();

}
