package com.jdroid.android.sample.ui.google.admob;

import androidx.fragment.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class FragmentBannerActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return FragmentBannerFragment.class;
	}
}
