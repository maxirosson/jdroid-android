package com.jdroid.android.application;

import androidx.annotation.WorkerThread;

public interface UpdateStep {

	@WorkerThread
	public void update();

	public Integer getVersionCode();
}
