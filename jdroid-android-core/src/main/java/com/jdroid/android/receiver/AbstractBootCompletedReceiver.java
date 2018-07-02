package com.jdroid.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.MainThread;

import com.jdroid.android.application.AbstractApplication;

public abstract class AbstractBootCompletedReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
			try {
				onBootCompleted();
			} catch (Exception e) {
				AbstractApplication.get().getExceptionHandler().logHandledException(e);
			}
		}
	}
	
	@MainThread
	protected abstract void onBootCompleted();
	
}
