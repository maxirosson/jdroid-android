package com.jdroid.android.sample.ui.loading;

import androidx.fragment.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class NonBlockingLoadingActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return NonBlockingLoadingFragment.class;
	}
}