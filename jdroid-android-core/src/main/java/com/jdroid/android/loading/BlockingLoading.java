package com.jdroid.android.loading;

import android.support.annotation.IdRes;

import com.jdroid.android.application.AbstractApplication;

public abstract class BlockingLoading implements ActivityLoading {

	private Boolean cancelable = AbstractApplication.get().isLoadingCancelable();
	private Integer loadingResId = null;

	public void setCancelable(Boolean cancelable) {
		this.cancelable = cancelable;
	}

	public Boolean isCancelable() {
		return cancelable;
	}

	public void setLoadingResId(@IdRes int loadingResId) {
		this.loadingResId = loadingResId;
	}

	public Integer getLoadingResId() {
		return loadingResId;
	}
}
