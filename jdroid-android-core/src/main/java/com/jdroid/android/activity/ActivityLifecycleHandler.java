package com.jdroid.android.activity;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

public class ActivityLifecycleHandler implements Application.ActivityLifecycleCallbacks {

	private int numStarted;

	@Override
	public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

	}

	@Override
	public void onActivityStarted(Activity activity) {
		numStarted++;
	}

	@Override
	public void onActivityResumed(Activity activity) {

	}

	@Override
	public void onActivityPaused(Activity activity) {

	}

	@Override
	public void onActivityStopped(Activity activity) {
		numStarted--;
	}

	@Override
	public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

	}

	@Override
	public void onActivityDestroyed(Activity activity) {

	}

	public Boolean isInBackground() {
		// http://stackoverflow.com/questions/3667022/checking-if-an-android-application-is-running-in-the-background/13809991#13809991
		return numStarted == 0;
	}
}
