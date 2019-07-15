package com.jdroid.android.sample.ui.navdrawer;

import android.os.Bundle;
import android.view.View;

import com.jdroid.android.activity.ActivityLauncher;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;

public class NavDrawerFragment extends AbstractFragment {

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.navdrawer_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		findView(R.id.leftCustomNavDrawer).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityLauncher.INSTANCE.startActivity(getActivity(), LeftCustomNavDrawerActivity.class);
			}
		});
		findView(R.id.rightCustomNavDrawer).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityLauncher.INSTANCE.startActivity(getActivity(), RightCustomNavDrawerActivity.class);
			}
		});
		findView(R.id.userNavDrawer).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityLauncher.INSTANCE.startActivity(getActivity(), UserNavDrawerActivity.class);
			}
		});
		findView(R.id.noNavDrawer).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityLauncher.INSTANCE.startActivity(getActivity(), NoNavDrawerActivity.class);
			}
		});
	}
}
