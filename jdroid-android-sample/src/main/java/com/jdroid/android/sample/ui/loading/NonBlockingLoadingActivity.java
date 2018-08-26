package com.jdroid.android.sample.ui.loading;

import android.support.v4.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class NonBlockingLoadingActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return NonBlockingLoadingFragment.class;
	}
}