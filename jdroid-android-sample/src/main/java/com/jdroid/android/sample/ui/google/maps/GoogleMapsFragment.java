package com.jdroid.android.sample.ui.google.maps;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.jdroid.android.activity.ActivityLauncher;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;

public class GoogleMapsFragment extends AbstractFragment {

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.google_maps_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		findView(R.id.fullMap).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityLauncher.INSTANCE.startActivity(getActivity(), MapActivity.class);
			}
		});

		findView(R.id.liteModeMap).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityLauncher.INSTANCE.startActivity(getActivity(), LiteModeMapActivity.class);
			}
		});

		findView(R.id.streetView).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityLauncher.INSTANCE.startActivity(getActivity(), StreetViewActivity.class);
			}
		});
	}
}
