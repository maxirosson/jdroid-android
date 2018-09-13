package com.jdroid.android.sample.ui.tablets;

import android.support.v4.app.Fragment;

import com.jdroid.android.activity.HorizontalFragmentsContainerActivity;

public class TabletActivity extends HorizontalFragmentsContainerActivity {

	@Override
	protected Class<? extends Fragment> getLeftFragmentClass() {
		return LeftTabletFragment.class;
	}

	@Override
	protected Class<? extends Fragment> getRightFragmentClass() {
		return RightTabletFragment.class;
	}
}
