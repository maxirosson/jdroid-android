package com.jdroid.android.firebase.remoteconfig;

import androidx.fragment.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class FirebaseRemoteConfigActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return FirebaseRemoteConfigFragment.class;
	}
}