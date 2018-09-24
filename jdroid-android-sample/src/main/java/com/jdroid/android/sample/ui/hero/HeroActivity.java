package com.jdroid.android.sample.ui.hero;

import androidx.fragment.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class HeroActivity extends FragmentContainerActivity {

	/**
	 * @see FragmentContainerActivity#getFragmentClass()
	 */
	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return HeroFragment.class;
	}
}