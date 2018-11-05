package com.jdroid.android.sample.ui.shortcuts;

import com.jdroid.android.activity.FragmentContainerActivity;

import androidx.fragment.app.Fragment;

public class AppShortcutsActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return AppShortcutsFragment.class;
	}
}