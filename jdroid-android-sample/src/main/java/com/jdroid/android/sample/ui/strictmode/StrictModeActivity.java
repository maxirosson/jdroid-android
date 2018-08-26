package com.jdroid.android.sample.ui.strictmode;

import android.support.v4.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class StrictModeActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return StrictModeFragment.class;
	}
}
