package com.jdroid.android.sample.ui.analytics;

import androidx.fragment.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class AnalyticsActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return AnalyticsFragment.class;
	}
}
