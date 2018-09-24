package com.jdroid.android.sample.ui.recyclerview;

import androidx.fragment.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class ComplexRecyclerActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return ComplexRecyclerFragment.class;
	}
}