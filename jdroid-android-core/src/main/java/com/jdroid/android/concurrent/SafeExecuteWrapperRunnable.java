package com.jdroid.android.concurrent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.support.v4.app.Fragment;

import com.jdroid.android.utils.AndroidUtils;

/**
 * Runnable implementation to wrap runnables to run them safely in the UI thread from a fragment. This class only call
 * the "run()" method of the wrapped runnable if the fragment is not detached.
 * 
 */
public class SafeExecuteWrapperRunnable implements Runnable {
	
	private Fragment fragment;
	private Runnable runnable;
	
	public SafeExecuteWrapperRunnable(Fragment fragment, Runnable runnable) {
		this.fragment = fragment;
		this.runnable = runnable;
	}
	
	@Override
	public void run() {
		if (fragment.getActivity() != null && !isActivityDestroyed(fragment.getActivity()) && !fragment.isDetached()) {
			runnable.run();
		}
	}

	@SuppressLint("NewApi")
	private Boolean isActivityDestroyed(Activity activity) {
		return AndroidUtils.getApiLevel() >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed();
	}
}
