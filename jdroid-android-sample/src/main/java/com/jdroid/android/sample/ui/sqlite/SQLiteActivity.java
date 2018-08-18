package com.jdroid.android.sample.ui.sqlite;

import android.support.v4.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class SQLiteActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return SQLiteFragment.class;
	}
}
