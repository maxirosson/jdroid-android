package com.jdroid.android.sample.ui.appinvite;

import android.support.v4.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class AppInviteActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return AppInviteFragment.class;
	}
}
