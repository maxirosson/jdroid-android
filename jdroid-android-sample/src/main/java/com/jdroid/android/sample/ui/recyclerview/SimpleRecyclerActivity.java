package com.jdroid.android.sample.ui.recyclerview;

import androidx.fragment.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class SimpleRecyclerActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return SimpleRecyclerFragment.class;
	}
}