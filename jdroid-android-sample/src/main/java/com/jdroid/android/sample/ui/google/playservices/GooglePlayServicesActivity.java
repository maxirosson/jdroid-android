package com.jdroid.android.sample.ui.google.playservices;

import com.jdroid.android.activity.FragmentContainerActivity;

import androidx.fragment.app.Fragment;

public class GooglePlayServicesActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return GooglePlayServicesFragment.class;
	}

	@Override
	public boolean isGooglePlayServicesVerificationEnabled() {
		return true;
	}
}