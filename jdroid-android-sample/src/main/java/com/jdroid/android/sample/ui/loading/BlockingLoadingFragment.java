package com.jdroid.android.sample.ui.loading;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jdroid.android.androidx.lifecycle.Resource;
import com.jdroid.android.androidx.lifecycle.ResourceObserver;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.androidx.SampleRepository;
import com.jdroid.android.sample.androidx.SampleViewModel;
import com.jdroid.android.sample.database.room.SampleEntity;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class BlockingLoadingFragment extends AbstractFragment {

	private SampleViewModel sampleViewModel;
	private Observer<Resource<SampleEntity>> observer;

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.blocking_loading_fragment;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		observer = new ResourceObserver<SampleEntity>() {

			@Override
			protected void onDataChanged(SampleEntity data) {
			}

			@Override
			protected void onStartLoading(@Nullable SampleEntity data) {
				getActivityIf().showLoading();
			}

			@Override
			protected void onStopLoading(@Nullable SampleEntity data) {
				getActivityIf().dismissLoading();
			}

			@Override
			protected AbstractFragment getFragment() {
				return BlockingLoadingFragment.this;
			}
		};
		sampleViewModel = ViewModelProviders.of(this).get(SampleViewModel.class);
		execute();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		Button fail = findView(R.id.fail);
		fail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				execute();
			}
		});
	}

	private void execute() {
		if (sampleViewModel.getSampleEntity() != null) {
			sampleViewModel.getSampleEntity().removeObserver(observer);
		}
		sampleViewModel.setFailLoadFromNetwork(true);
		sampleViewModel.setLoadFromNetworkDelaySeconds(5);
		sampleViewModel.load(SampleRepository.ID, true).observe(this, observer);
	}

}
