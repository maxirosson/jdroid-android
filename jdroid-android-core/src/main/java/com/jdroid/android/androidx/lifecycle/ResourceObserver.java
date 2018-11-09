package com.jdroid.android.androidx.lifecycle;

import com.jdroid.android.exception.AbstractErrorDisplayer;
import com.jdroid.android.exception.DialogErrorDisplayer;
import com.jdroid.android.fragment.AbstractFragment;

import androidx.lifecycle.Observer;

public abstract class ResourceObserver<T> implements Observer<Resource<T>> {

	@Override
	public void onChanged(Resource<T> resource) {
		if (resource != null) {
			if (resource.getStatus().equals(Resource.Status.LOADING)) {
				if (resource.getData() != null) {
					showLoading(resource);
				}
			} else if (resource.getStatus().equals(Resource.Status.SUCCESS)) {
				onSuccess(resource);
				dismissLoading(resource);
			} else if (resource.getStatus().equals(Resource.Status.ERROR)) {
				dismissLoading(resource);
				onError(resource);
			}
		}
	}

	protected void showLoading(Resource<T> resource) {
		getFragment().showLoading();
	}

	protected void dismissLoading(Resource<T> resource) {
		getFragment().dismissLoading();
	}

	protected abstract void onSuccess(Resource<T> resource);

	protected void onError(Resource<T> resource) {
		DialogErrorDisplayer.markAsNotGoBackOnError(resource.getException());
		AbstractErrorDisplayer.getErrorDisplayer(resource.getException()).displayError(getFragment().getActivity(), resource.getException());
	}

	protected abstract AbstractFragment getFragment();

}
