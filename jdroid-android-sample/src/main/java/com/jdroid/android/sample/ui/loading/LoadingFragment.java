package com.jdroid.android.sample.ui.loading;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.jdroid.android.activity.ActivityLauncher;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;

public class LoadingFragment extends AbstractFragment {

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.loading_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		findView(R.id.blockingLoading).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityLauncher.startActivity(getActivity(), new Intent(getActivity(), BlockingLoadingActivity.class));
			}
		});
		findView(R.id.customBlockingLoading).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityLauncher.startActivity(getActivity(), new Intent(getActivity(), CustomActivityLoadingActivity.class));
			}
		});
		findView(R.id.nonBlockingLoading).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityLauncher.startActivity(getActivity(), new Intent(getActivity(), NonBlockingLoadingActivity.class));
			}
		});
		findView(R.id.swipeRefresLoading).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityLauncher.startActivity(getActivity(), new Intent(getActivity(), SwipeRefreshLoadingActivity.class));
			}
		});
	}
}
