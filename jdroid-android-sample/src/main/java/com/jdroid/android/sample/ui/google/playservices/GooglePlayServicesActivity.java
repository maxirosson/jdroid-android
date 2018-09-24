package com.jdroid.android.sample.ui.google.playservices;

import androidx.fragment.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class GooglePlayServicesActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return GooglePlayServicesFragment.class;
	}

	@Override
	public Boolean isGooglePlayServicesVerificationEnabled() {
		return true;
	}
}