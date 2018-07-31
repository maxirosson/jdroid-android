package com.jdroid.android.sample.ui.service;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;

import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.ui.usecases.SampleUseCase;
import com.jdroid.android.usecase.service.UseCaseService;
import com.jdroid.java.utils.TypeUtils;

import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

public class ServiceFragment extends AbstractFragment {

	private CheckBox failCheckBox;
	private EditText delayEditText;

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.service_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		failCheckBox = findView(R.id.fail);
		delayEditText = findView(R.id.delay);
		
		findView(R.id.workerService).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("a", "1");
				intent.putExtra("fail", failCheckBox.isChecked());
				SampleWorkerService.runIntentInService(intent);
			}
		});
		findView(R.id.useCaseService).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SampleUseCase sampleUseCase = new SampleUseCase();
				sampleUseCase.setFail(failCheckBox.isChecked());
				UseCaseService.execute(sampleUseCase);
			}
		});
		findView(R.id.sampleWorker1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				OneTimeWorkRequest.Builder sampleWorkRequestBuilder = new OneTimeWorkRequest.Builder(SampleWorker1.class);
				
				Data.Builder dataBuilder = createCommonDataBuilder();
				dataBuilder.putString("a", "3");
				dataBuilder.putBoolean("fail", failCheckBox.isChecked());
				sampleWorkRequestBuilder.setInputData(dataBuilder.build());
				
				Constraints.Builder constrainsBuilder = new Constraints.Builder();
				constrainsBuilder.setRequiredNetworkType(NetworkType.CONNECTED);
				sampleWorkRequestBuilder.setConstraints(constrainsBuilder.build());
				
				enqueue(sampleWorkRequestBuilder);
			}
		});
		findView(R.id.sampleWorker2).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				OneTimeWorkRequest.Builder sampleWorkRequestBuilder = new OneTimeWorkRequest.Builder(SampleWorker2.class);
				
				Data.Builder dataBuilder = createCommonDataBuilder();
				dataBuilder.putString("a", "4");
				sampleWorkRequestBuilder.setInputData(dataBuilder.build());
				
				enqueue(sampleWorkRequestBuilder);
			}
		});
		findView(R.id.sampleWorker3).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				OneTimeWorkRequest.Builder sampleWorkRequestBuilder = new OneTimeWorkRequest.Builder(SampleWorker3.class);
				
				Data.Builder dataBuilder = createCommonDataBuilder();
				dataBuilder.putString("a", "5");
				sampleWorkRequestBuilder.setInputData(dataBuilder.build());
				
				enqueue(sampleWorkRequestBuilder);
			}
		});
		findView(R.id.sampleWorker4).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				OneTimeWorkRequest.Builder sampleWorkRequestBuilder = new OneTimeWorkRequest.Builder(SampleWorker4.class);
				
				Data.Builder dataBuilder = createCommonDataBuilder();
				sampleWorkRequestBuilder.setInputData(dataBuilder.build());
				
				enqueue(sampleWorkRequestBuilder);
			}
		});
		
		findView(R.id.periodicWorker).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PeriodicWorkRequest.Builder sampleWorkRequestBuilder = new PeriodicWorkRequest.Builder(SampleWorker1.class, 15, TimeUnit.MINUTES);
				
				Data.Builder dataBuilder = createCommonDataBuilder();
				sampleWorkRequestBuilder.setInputData(dataBuilder.build());
				
				WorkManager.getInstance().enqueue(sampleWorkRequestBuilder.build());
			}
		});
		
		findView(R.id.cancelAllWork).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				WorkManager.getInstance().cancelAllWork();
			}
		});
	}
	
	private void enqueue(OneTimeWorkRequest.Builder sampleWorkRequestBuilder) {
		Integer delay = TypeUtils.getSafeInteger(delayEditText.getText());
		if (delay != null) {
			sampleWorkRequestBuilder.setInitialDelay(delay, TimeUnit.SECONDS);
		}
		
		WorkManager.getInstance().enqueue(sampleWorkRequestBuilder.build());
	}
	
	private Bundle createServiceCommandBundle() {
		Bundle bundle = new Bundle();
		bundle.putBoolean("fail", failCheckBox.isChecked());
		Integer delay = TypeUtils.getInteger(delayEditText.getText());
		if (delay != null) {
			bundle.putInt("delay", delay);
		}
		return bundle;
	}
	
	private Data.Builder createCommonDataBuilder() {
		Data.Builder dataBuilder = new Data.Builder();
		dataBuilder.putBoolean("fail", failCheckBox.isChecked());
		return dataBuilder;
	}
}
