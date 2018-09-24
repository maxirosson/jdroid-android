package com.jdroid.android.sample.ui.notifications;

import androidx.fragment.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class NotificationsActivity extends FragmentContainerActivity {

	/**
	 * @see com.jdroid.android.activity.FragmentContainerActivity#getFragmentClass()
	 */
	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return NotificationsFragment.class;
	}
}
