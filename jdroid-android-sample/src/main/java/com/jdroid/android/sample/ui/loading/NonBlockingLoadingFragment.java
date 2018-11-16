package com.jdroid.android.sample.ui.loading;

import android.os.Bundle;

import com.jdroid.android.androidx.lifecycle.Resource;
import com.jdroid.android.androidx.lifecycle.ResourceObserver;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.loading.FragmentLoading;
import com.jdroid.android.loading.NonBlockingLoading;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.androidx.SampleRepository;
import com.jdroid.android.sample.androidx.SampleViewModel;
import com.jdroid.android.sample.database.room.SampleEntity;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class NonBlockingLoadingFragment extends AbstractFragment {

	private SampleViewModel sampleViewModel;
	private Observer<Resource<SampleEntity>> observer;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		observer = new ResourceObserver<SampleEntity>() {

			@Override
			protected void onDataChanged(SampleEntity data) {
			}

			@Override
			protected void onStartLoading(@Nullable SampleEntity data) {
				getFragment().showLoading();
			}

			@Override
			protected void onStopLoading(@Nullable SampleEntity data) {
				getFragment().dismissLoading();
			}

			@Override
			protected AbstractFragment getFragment() {
				return NonBlockingLoadingFragment.this;
			}
		};
		sampleViewModel = ViewModelProviders.of(this).get(SampleViewModel.class);
		sampleViewModel.load(SampleRepository.ID, true, false, 5).observe(this, observer);
	}

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.non_blocking_loading_fragment;
	}

	@Override
	public FragmentLoading getDefaultLoading() {
		return new NonBlockingLoading();
	}
}
