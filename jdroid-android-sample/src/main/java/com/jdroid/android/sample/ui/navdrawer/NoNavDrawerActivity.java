package com.jdroid.android.sample.ui.navdrawer;

import com.jdroid.android.activity.FragmentContainerActivity;

import androidx.fragment.app.Fragment;

public class NoNavDrawerActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return NoNavDrawerFragment.class;
	}

	@Override
	public boolean isNavDrawerEnabled() {
		return false;
	}
}