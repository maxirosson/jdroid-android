package com.jdroid.android.sample.ui.firebase.dynamiclinks;

import androidx.fragment.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class DynamicLinksActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return DynamicLinksFragment.class;
	}
}