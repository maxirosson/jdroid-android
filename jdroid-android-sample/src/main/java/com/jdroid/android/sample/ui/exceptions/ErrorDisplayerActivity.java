package com.jdroid.android.sample.ui.exceptions;

import androidx.fragment.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class ErrorDisplayerActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return ErrorDisplayerFragment.class;
	}
}
