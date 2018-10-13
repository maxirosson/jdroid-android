package com.jdroid.android.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.View;

public abstract class FragmentDelegate {

	private Fragment fragment;

	public FragmentDelegate(Fragment fragment) {
		this.fragment = fragment;
	}

	public void onCreate(Bundle savedInstanceState) {
		// Do Nothing
	}

	public void onViewCreated(View view, Bundle savedInstanceState) {
		// Do Nothing
	}

	public void onResume() {
		// Do Nothing
	}

	public void onBeforePause() {
		// Do Nothing
	}


	public void onBeforeDestroy() {
		// Do Nothing
	}

	public Fragment getFragment() {
		return fragment;
	}
}