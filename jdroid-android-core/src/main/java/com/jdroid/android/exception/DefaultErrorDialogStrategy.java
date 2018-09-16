package com.jdroid.android.exception;

import android.app.Activity;

public class DefaultErrorDialogStrategy implements ErrorDialogStrategy {

	private static final long serialVersionUID = -6175944102394797645L;
	public boolean goBackOnError;

	@Override
	public void onPositiveClick(Activity activity) {
		if (goBackOnError && activity != null) {
			activity.finish();
		}
	}

	public void setGoBackOnError(boolean goBackOnError) {
		this.goBackOnError = goBackOnError;
	}

}
