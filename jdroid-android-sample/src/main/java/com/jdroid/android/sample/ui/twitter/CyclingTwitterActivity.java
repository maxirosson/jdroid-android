package com.jdroid.android.sample.ui.twitter;

import androidx.fragment.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class CyclingTwitterActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return CyclingTwitterFragment.class;
	}
}