package com.jdroid.android.google;

import androidx.annotation.NonNull;

import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.jdroid.android.application.AbstractApplication;

public abstract class SafeResultCallback<R extends Result> implements ResultCallback<R> {

	@Override
	public final void onResult(@NonNull R result) {
		try {
			if (result.getStatus().isSuccess()) {
				onSuccessResult(result);
			} else {
				onFailedResult(result);
			}
		} catch (Exception e) {
			AbstractApplication.get().getExceptionHandler().logHandledException(e);
		}
	}

	public abstract void onSuccessResult(@NonNull R result);

	public abstract void onFailedResult(@NonNull R result);
}
