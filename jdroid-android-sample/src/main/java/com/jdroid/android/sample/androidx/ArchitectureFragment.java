package com.jdroid.android.sample.androidx;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.jdroid.android.androidx.lifecycle.Resource;
import com.jdroid.android.androidx.lifecycle.ResourceObserver;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.loading.FragmentLoading;
import com.jdroid.android.loading.NonBlockingLoading;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.database.room.SampleEntity;
import com.jdroid.java.utils.TypeUtils;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


public class ArchitectureFragment extends AbstractFragment {

	private static Boolean failLoadFromNetwork = false;
	private static Boolean failLoadFromDb = false;
	private static Boolean failSaveToDb = false;

	private SampleViewModel sampleViewModel;
	private Observer<Resource<SampleEntity>> observer;

	private EditText loadFromNetworkDelaySecondsEditText;
	private EditText loadFromDbDelaySecondsEditText;
	private EditText saveToDbDelaySecondsEditText;

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.architecture_fragment;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		observer = new ResourceObserver<SampleEntity>() {

			@Override
			protected void onDataChanged(SampleEntity data) {
				TextView result = findView(R.id.result);
				result.setText(data.toString());
			}

			@Override
			protected void onStarting() {
				findView(R.id.internalLoading).setVisibility(View.VISIBLE);
			}

			@Override
			protected void onStartLoading(@Nullable SampleEntity data) {
				super.onStartLoading(data);
				findView(R.id.internalLoading).setVisibility(View.VISIBLE);
			}

			@Override
			protected void onStopLoading(@Nullable SampleEntity data) {
				super.onStopLoading(data);
				findView(R.id.internalLoading).setVisibility(View.GONE);
			}

			@Override
			protected AbstractFragment getFragment() {
				return ArchitectureFragment.this;
			}
		};
		sampleViewModel = ViewModelProviders.of(this).get(SampleViewModel.class);
		execute(false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		CheckBox failLoadFromNetworkCheckBox = findView(R.id.failLoadFromNetwork);
		failLoadFromNetworkCheckBox.setChecked(failLoadFromNetwork);
		failLoadFromNetworkCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				failLoadFromNetwork = isChecked;
			}
		});

		loadFromNetworkDelaySecondsEditText = findView(R.id.loadFromNetworkDelaySeconds);
		loadFromNetworkDelaySecondsEditText.setText("5");

		CheckBox failLoadFromDbCheckBox = findView(R.id.failLoadFromDb);
		failLoadFromDbCheckBox.setChecked(failLoadFromDb);
		failLoadFromDbCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				failLoadFromDb = isChecked;
			}
		});

		loadFromDbDelaySecondsEditText = findView(R.id.loadFromDbDelaySeconds);
		loadFromDbDelaySecondsEditText.setText("5");

		CheckBox failSaveToDbCheckBox = findView(R.id.failSaveToDb);
		failSaveToDbCheckBox.setChecked(failSaveToDb);
		failSaveToDbCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				failSaveToDb = isChecked;
			}
		});

		saveToDbDelaySecondsEditText = findView(R.id.saveToDbDelaySeconds);
		saveToDbDelaySecondsEditText.setText("5");

		findView(R.id.execute).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				execute(true);
			}
		});
	}

	private void execute(Boolean forceRefresh) {
		if (sampleViewModel.getSampleEntity() != null && forceRefresh) {
			sampleViewModel.getSampleEntity().removeObserver(observer);
		}
		sampleViewModel.setFailLoadFromNetwork(failLoadFromNetwork);
		sampleViewModel.setLoadFromNetworkDelaySeconds(TypeUtils.getInteger(loadFromNetworkDelaySecondsEditText.getText()));
		sampleViewModel.setFailLoadFromDb(failLoadFromDb);
		sampleViewModel.setLoadFromDbDelaySeconds(TypeUtils.getInteger(loadFromDbDelaySecondsEditText.getText()));
		sampleViewModel.setFailSaveToDb(failSaveToDb);
		sampleViewModel.setSaveToDbDelaySeconds(TypeUtils.getInteger(saveToDbDelaySecondsEditText.getText()));
		sampleViewModel.load(SampleRepository.ID, forceRefresh).observe(this, observer);
	}

	@Override
	public FragmentLoading getDefaultLoading() {
		return new NonBlockingLoading();
	}
}
