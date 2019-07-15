package com.jdroid.android.sample.ui.recyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jdroid.android.activity.ActivityLauncher;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;

public class RecyclerViewFragment extends AbstractFragment {

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.recycler_view_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		findView(R.id.simpleRecycler).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityLauncher.INSTANCE.startActivity(getActivity(), new Intent(getActivity(), SimpleRecyclerActivity.class));
			}
		});
		findView(R.id.complexRecycler).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityLauncher.INSTANCE.startActivity(getActivity(), new Intent(getActivity(), ComplexRecyclerActivity.class));
			}
		});
		findView(R.id.paginatedRecycler).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityLauncher.INSTANCE.startActivity(getActivity(), new Intent(getActivity(), PaginatedRecyclerActivity.class));
			}
		});
		findView(R.id.paginatedGridRecycler).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityLauncher.INSTANCE.startActivity(getActivity(), new Intent(getActivity(), PaginatedGridRecyclerActivity.class));
			}
		});
		findView(R.id.searchPaginatedRecycler).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityLauncher.INSTANCE.startActivity(getActivity(), new Intent(getActivity(), SearchPaginatedRecyclerActivity.class));
			}
		});
		findView(R.id.autofitGridLayoutRecycler).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityLauncher.INSTANCE.startActivity(getActivity(), new Intent(getActivity(), AutofitGridLayoutRecyclerActivity.class));
			}
		});
	}
}

