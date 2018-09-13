package com.jdroid.android.sample.ui.google.maps;

import android.support.v4.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class StreetViewActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return StreetViewFragment.class;
	}
}
