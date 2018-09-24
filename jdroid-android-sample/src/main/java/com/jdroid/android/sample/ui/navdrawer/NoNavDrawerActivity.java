package com.jdroid.android.sample.ui.navdrawer;

import androidx.fragment.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class NoNavDrawerActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return NoNavDrawerFragment.class;
	}

	@Override
	public Boolean isNavDrawerEnabled() {
		return false;
	}
}