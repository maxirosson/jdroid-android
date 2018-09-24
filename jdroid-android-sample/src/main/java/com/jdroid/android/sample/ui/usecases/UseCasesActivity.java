package com.jdroid.android.sample.ui.usecases;

import androidx.fragment.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class UseCasesActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return UseCasesFragment.class;
	}
}
