package com.jdroid.android.sample.ui.cardview;

import android.os.Bundle;
import android.view.View;

import com.jdroid.android.activity.ActivityLauncher;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;

public class CardViewFragment extends AbstractFragment {

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.cardview_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		findView(R.id.simpleCardView).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityLauncher.startActivity(getActivity(), SimpleCardViewActivity.class);
			}
		});

		findView(R.id.cardViewInsideRecyclerView).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityLauncher.startActivity(getActivity(), CardViewRecyclerViewActivity.class);
			}
		});
	}
}
