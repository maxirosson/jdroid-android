package com.jdroid.android.intent;

import android.content.Intent;
import android.content.IntentFilter;
import com.jdroid.android.AbstractApplication;

public class ClearTaskIntent {
	
	private static final String CLEAR_TASK_ACTION = "com.android.CLEAR_TASK_ACTION";
	public static final String REQUIRES_AUTHENTICATION_EXTRA = "requiresAuthentication";
	
	public static void execute(Boolean requiresAuthentication) {
		Intent broadcastIntent = new Intent();
		broadcastIntent.putExtra(REQUIRES_AUTHENTICATION_EXTRA, requiresAuthentication);
		broadcastIntent.setAction(CLEAR_TASK_ACTION);
		AbstractApplication.get().sendBroadcast(broadcastIntent);
	}
	
	public static IntentFilter newIntentFilter() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(CLEAR_TASK_ACTION);
		return intentFilter;
	}
	
}
