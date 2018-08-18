package com.jdroid.android.sample.ui.leakcanary;

import android.os.Bundle;
import android.view.View;

import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;

public class LeakCanaryFragment extends AbstractFragment {

	public static AbstractFragment LEAK;

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.leakcanary_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		LEAK = this;
	}
}
