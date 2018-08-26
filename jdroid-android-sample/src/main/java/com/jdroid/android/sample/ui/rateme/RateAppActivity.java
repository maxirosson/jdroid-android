package com.jdroid.android.sample.ui.rateme;

import android.support.v4.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class RateAppActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return RateAppFragment.class;
	}
}
