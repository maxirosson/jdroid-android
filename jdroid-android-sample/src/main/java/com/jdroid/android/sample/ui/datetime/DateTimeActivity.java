package com.jdroid.android.sample.ui.datetime;

import androidx.fragment.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class DateTimeActivity extends FragmentContainerActivity {

	/**
	 * @see FragmentContainerActivity#getFragmentClass()
	 */
	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return DateTimeFragment.class;
	}
}
