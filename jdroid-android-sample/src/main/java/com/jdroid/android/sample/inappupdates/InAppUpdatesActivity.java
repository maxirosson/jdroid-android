package com.jdroid.android.sample.inappupdates;

import com.jdroid.android.activity.FragmentContainerActivity;

import androidx.fragment.app.Fragment;

public class InAppUpdatesActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return InAppUpdatesFragment.class;
	}
}