package com.jdroid.android.exception;

import androidx.fragment.app.FragmentActivity;

public interface ErrorDisplayer {

	public void displayError(FragmentActivity activity, Throwable throwable);

}
