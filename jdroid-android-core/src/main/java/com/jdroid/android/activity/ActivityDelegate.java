package com.jdroid.android.activity;

import android.os.Bundle;

public abstract class ActivityDelegate {

	private AbstractFragmentActivity activity;

	public ActivityDelegate(AbstractFragmentActivity activity) {
		this.activity = activity;
	}

	public void onCreate(Bundle savedInstanceState) {
		// Do Nothing
	}

	public void onStart() {
		// Do Nothing
	}

	public void onResume() {
		// Do Nothing
	}

	public void onBeforePause() {
		// Do Nothing
	}

	public void onPause() {
		// Do Nothing
	}

	public void onStop() {
		// Do Nothing
	}

	public void onBeforeDestroy() {
		// Do Nothing
	}

	public void onDestroy() {
		// Do Nothing
	}

	public AbstractFragmentActivity getActivity() {
		return activity;
	}
}
