package com.jdroid.android.sample.ui.analytics;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.analytics.AppAnalyticsSender;

public class AnalyticsFragment extends AbstractFragment {

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.analytics_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		findView(R.id.sendExampleEvent).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppAnalyticsSender.get().trackExampleEvent();
			}
		});

		findView(R.id.sendExampleTransaction).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppAnalyticsSender.get().trackExampleTransaction();
			}
		});

		findView(R.id.sendExampleTiming).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppAnalyticsSender.get().trackExampleTiming();
			}
		});
	}
}
