package com.jdroid.android.sample.ui.service;

import android.content.Context;
import android.os.Bundle;

import com.jdroid.android.firebase.jobdispatcher.ServiceCommand;
import com.jdroid.java.concurrent.ExecutorUtils;

public class SampleServiceCommand3 extends ServiceCommand {

	@Override
	protected boolean execute(Context context, Bundle bundle) {
		ExecutorUtils.sleep(30);
		return false;
	}
}
