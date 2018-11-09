package com.jdroid.android.sample.androidx;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jdroid.android.androidx.lifecycle.Resource;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.database.room.SampleEntity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


public class ArchitectureFragment extends AbstractFragment {

	private SampleViewModel sampleViewModel;
	private Observer<Resource<SampleEntity>> observer;

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.architecture_fragment;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		sampleViewModel = ViewModelProviders.of(this).get(SampleViewModel.class);
		sampleViewModel.init("1");
		observer = new Observer<Resource<SampleEntity>>() {

			@Override
			public void onChanged(Resource<SampleEntity> resource) {
				if (resource != null) {
					TextView result = findView(R.id.result);
					if (resource.getStatus().equals(Resource.Status.LOADING)) {
						result.setText(R.string.loading);
					} else if (resource.getStatus().equals(Resource.Status.SUCCESS)) {
						result.setText(resource.getData().toString());
					} else if (resource.getStatus().equals(Resource.Status.ERROR)) {
						result.setText(resource.getException().getClass().getSimpleName() + " : " + resource.getException().getMessage());
					}
				}
			}
		};
		sampleViewModel.getSampleEntity().observe(this, observer);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		findView(R.id.refresh).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sampleViewModel.getSampleEntity().removeObserver(observer);
				sampleViewModel.refresh("1");
				sampleViewModel.getSampleEntity().observe(ArchitectureFragment.this, observer);
			}
		});
	}
}
