package com.jdroid.android.sample.ui.toasts;

import androidx.fragment.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class ToastsActivity extends FragmentContainerActivity {

	/**
	 * @see com.jdroid.android.activity.FragmentContainerActivity#getFragmentClass()
	 */
	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return ToastsFragment.class;
	}
}
