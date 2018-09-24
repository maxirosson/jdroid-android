package com.jdroid.android.sample.ui.google.signin;

import androidx.fragment.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class GoogleSignInActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return GoogleSignInFragment.class;
	}
}