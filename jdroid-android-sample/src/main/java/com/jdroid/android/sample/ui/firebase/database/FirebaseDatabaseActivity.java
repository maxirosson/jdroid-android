package com.jdroid.android.sample.ui.firebase.database;

import androidx.fragment.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class FirebaseDatabaseActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return FirebaseDatabaseFragment.class;
	}
}