package com.jdroid.android.sample.ui.loading;

import androidx.fragment.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class SwipeRefreshLoadingActivity extends FragmentContainerActivity {

	/**
	 * @see com.jdroid.android.activity.FragmentContainerActivity#getFragmentClass()
	 */
	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return SwipeRefreshLoadingFragment.class;
	}
}