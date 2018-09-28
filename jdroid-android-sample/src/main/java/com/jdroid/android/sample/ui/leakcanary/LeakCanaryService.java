package com.jdroid.android.sample.ui.leakcanary;

import android.content.Intent;
import androidx.annotation.NonNull;

import com.jdroid.android.service.AbstractWorkerService;

public class LeakCanaryService extends AbstractWorkerService {

	public static AbstractWorkerService LEAK;

	@Override
	protected void doExecute(@NonNull Intent intent) {
		LEAK = this;
	}
}
