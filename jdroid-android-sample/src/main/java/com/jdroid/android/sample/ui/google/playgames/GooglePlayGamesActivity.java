package com.jdroid.android.sample.ui.google.playgames;

import android.support.v4.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class GooglePlayGamesActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return GooglePlayGamesFragment.class;
	}
}