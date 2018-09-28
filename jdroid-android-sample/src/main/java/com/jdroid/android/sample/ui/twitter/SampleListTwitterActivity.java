package com.jdroid.android.sample.ui.twitter;

import androidx.fragment.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class SampleListTwitterActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return SampleListTwitterFragment.class;
	}
}