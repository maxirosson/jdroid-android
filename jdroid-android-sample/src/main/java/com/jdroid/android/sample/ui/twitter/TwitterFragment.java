package com.jdroid.android.sample.ui.twitter;

import android.os.Bundle;
import android.view.View;

import com.jdroid.android.activity.ActivityLauncher;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;

public class TwitterFragment extends AbstractFragment {
	
	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.twitter_fragment;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		findView(R.id.listTweetHelper).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityLauncher.startActivity(getActivity(), SampleListTwitterActivity.class);
			}
		});
		
		findView(R.id.cyclingTweetHelper).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityLauncher.startActivity(getActivity(), CyclingTwitterActivity.class);
			}
		});
		
	}
}
