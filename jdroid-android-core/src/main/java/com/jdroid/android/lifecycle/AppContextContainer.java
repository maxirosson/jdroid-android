package com.jdroid.android.lifecycle;

import android.content.Context;

public class AppContextContainer {
	
	private static Context applicationContext;
	
	public static Context getApplicationContext() {
		return applicationContext;
	}
	
	static void setApplicationContext(Context context) {
		applicationContext = context;
	}
}
