package com.jdroid.android.sample.ui.strictmode;

import android.os.Bundle;
import android.view.View;

import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;
import com.jdroid.android.utils.SharedPreferencesHelper;

public class StrictModeFragment extends AbstractFragment {

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.strictmode_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		findView(R.id.diskAccessOnMainThread).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferencesHelper.get().savePreference("a", "b");
				SharedPreferencesHelper.get("asdas").loadAllPreferences();
			}
		});
	}
}
