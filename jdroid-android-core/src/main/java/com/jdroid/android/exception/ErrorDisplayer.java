package com.jdroid.android.exception;

import android.support.v4.app.FragmentActivity;

public interface ErrorDisplayer {

	public void displayError(FragmentActivity activity, Throwable throwable);

}
