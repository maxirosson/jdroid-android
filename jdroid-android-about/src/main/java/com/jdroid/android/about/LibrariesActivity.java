package com.jdroid.android.about;

import android.support.v4.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class LibrariesActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return AboutAppModule.get().getAboutContext().getLibrariesFragmentClass();
	}
}
