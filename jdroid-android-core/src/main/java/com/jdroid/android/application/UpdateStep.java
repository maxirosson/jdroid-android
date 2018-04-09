package com.jdroid.android.application;

import android.support.annotation.WorkerThread;

public interface UpdateStep {

	@WorkerThread
	public void update();

	public Integer getVersionCode();
}
