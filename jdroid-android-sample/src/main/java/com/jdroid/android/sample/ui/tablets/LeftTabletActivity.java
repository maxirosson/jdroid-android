package com.jdroid.android.sample.ui.tablets;

import androidx.fragment.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class LeftTabletActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return LeftTabletFragment.class;
	}
}
