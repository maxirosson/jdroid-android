package com.jdroid.android.androidx.viewmodel;

import com.jdroid.android.utils.TagUtils;
import com.jdroid.java.utils.LoggerUtils;

import androidx.annotation.CallSuper;
import androidx.lifecycle.ViewModel;

public abstract class AbstractViewModel extends ViewModel {

	@CallSuper
	@Override
	protected void onCleared() {
		super.onCleared();
		LoggerUtils.getLogger(getTag()).info("Clearing view model");
	}

	protected String getTag() {
		return TagUtils.getTag(getClass());
	}
}
