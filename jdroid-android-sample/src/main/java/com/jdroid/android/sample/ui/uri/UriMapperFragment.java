package com.jdroid.android.sample.ui.uri;

import android.os.Bundle;
import android.view.View;

import com.jdroid.android.activity.ActivityLauncher;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;

public class UriMapperFragment extends AbstractFragment {

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.uri_mapper_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		findView(R.id.singleTop).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ActivityLauncher.startActivity(getActivity(), UriMapperSingleTopActivity.class);

			}
		});

		findView(R.id.noFlags).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ActivityLauncher.startActivity(getActivity(), UriMapperNoFlagsActivity.class);

			}
		});

		findView(R.id.matchError).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ActivityLauncher.startActivity(getActivity(), MatchErrorActivity.class);

			}
		});

		findView(R.id.mainIntentError).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ActivityLauncher.startActivity(getActivity(), MainIntentErrorActivity.class);

			}
		});

		findView(R.id.defaultIntentError).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ActivityLauncher.startActivity(getActivity(), DefaulItntentErrorActivity.class);

			}
		});

		findView(R.id.matchNewActivity).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ActivityLauncher.startActivity(getActivity(), MatchNewActivity.class);

			}
		});

		findView(R.id.noMatchNewActivity).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ActivityLauncher.startActivity(getActivity(), NoMatchNewActivity.class);

			}
		});

		findView(R.id.matchSameActivity).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ActivityLauncher.startActivity(getActivity(), MatchSameActivity.class);

			}
		});

		findView(R.id.noMatchSameActivity).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ActivityLauncher.startActivity(getActivity(), NoMatchSameActivity.class);

			}
		});

		findView(R.id.matchNullIntent).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ActivityLauncher.startActivity(getActivity(), MatchNullIntentActivity.class);

			}
		});

		findView(R.id.noMatchNullIntent).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ActivityLauncher.startActivity(getActivity(), NoMatchNullIntentActivity.class);

			}
		});
	}
}
