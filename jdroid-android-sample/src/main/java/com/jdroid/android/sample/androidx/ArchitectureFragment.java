package com.jdroid.android.sample.androidx;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.jdroid.android.androidx.lifecycle.Resource;
import com.jdroid.android.androidx.lifecycle.ResourceObserver;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.loading.FragmentLoading;
import com.jdroid.android.loading.NonBlockingLoading;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.database.room.SampleEntity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


public class ArchitectureFragment extends AbstractFragment {

	private static Boolean failExecution = false;
	private static Boolean forceRefresh = false;

	private SampleViewModel sampleViewModel;
	private Observer<Resource<SampleEntity>> observer;

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.architecture_fragment;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		observer = new ResourceObserver<SampleEntity>() {

			@Override
			protected void onSuccess(Resource<SampleEntity> resource) {
				TextView result = findView(R.id.result);
				result.setText(resource.getData().toString());
			}

			@Override
			protected AbstractFragment getFragment() {
				return ArchitectureFragment.this;
			}
		};
		sampleViewModel = ViewModelProviders.of(this).get(SampleViewModel.class);
		execute();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		CheckBox failExecutionCheckBox = findView(R.id.failExecution);
		failExecutionCheckBox.setChecked(failExecution);
		failExecutionCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				failExecution = isChecked;
			}
		});

		CheckBox forceRefreshCheckBox = findView(R.id.forceRefresh);
		forceRefreshCheckBox.setChecked(forceRefresh);
		forceRefreshCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				forceRefresh = isChecked;
			}
		});

		findView(R.id.execute).setOnClickListener(new View.OnClickListener() {
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
		sampleViewModel.load(SampleRepository.ID, forceRefresh, failExecution).observe(this, observer);
	}

	@Override
	public FragmentLoading getDefaultLoading() {
		return new NonBlockingLoading();
	}
}
