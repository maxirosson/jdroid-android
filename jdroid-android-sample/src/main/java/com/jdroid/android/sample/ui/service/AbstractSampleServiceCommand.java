package com.jdroid.android.sample.ui.service;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Trigger;
import com.jdroid.android.firebase.jobdispatcher.ServiceCommand;

public abstract class AbstractSampleServiceCommand extends ServiceCommand {

	@NonNull
	@Override
	protected Job.Builder createJobBuilder(FirebaseJobDispatcher dispatcher, Bundle bundle) {
		Job.Builder builder = super.createJobBuilder(dispatcher, bundle);
		int delay = bundle.getInt("delay");
		if (delay != 0) {
			builder.setTrigger(Trigger.executionWindow(delay, delay + 10));
		}
		return builder;
	}
}
