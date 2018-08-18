package com.jdroid.android.exception;

import android.app.Activity;

import java.io.Serializable;

public interface ErrorDialogStrategy extends Serializable {

	public void onPositiveClick(Activity activity);
}
