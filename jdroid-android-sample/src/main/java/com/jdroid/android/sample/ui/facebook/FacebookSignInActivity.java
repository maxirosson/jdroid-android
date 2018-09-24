package com.jdroid.android.sample.ui.facebook;

import androidx.fragment.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class FacebookSignInActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return FacebookSignInFragment.class;
	}
}