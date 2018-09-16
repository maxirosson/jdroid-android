package com.jdroid.android.sample.ui.google.playservices;

import android.os.Bundle;
import android.view.View;

import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.google.GooglePlayServicesUtils;
import com.jdroid.android.sample.R;

public class GooglePlayServicesFragment extends AbstractFragment {

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.google_play_services_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		findView(R.id.checkGooglePlayServices).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				GooglePlayServicesUtils.checkGooglePlayServices(getActivity());
			}
		});

		findView(R.id.openGooglePlayServices).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				GooglePlayServicesUtils.launchGooglePlayServicesUpdate();
			}
		});
	}
}
