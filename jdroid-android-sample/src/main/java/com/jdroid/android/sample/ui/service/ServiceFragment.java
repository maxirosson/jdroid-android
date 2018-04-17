package com.jdroid.android.sample.ui.service;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.jdroid.android.firebase.jobdispatcher.ServiceCommand;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.ui.usecases.SampleUseCase;
import com.jdroid.android.usecase.service.UseCaseService;

public class ServiceFragment extends AbstractFragment {

	private CheckBox failCheckBox;

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.service_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		failCheckBox = findView(R.id.fail);
		
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
		findView(R.id.firebaseJobService).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putString("a", "2");
				bundle.putBoolean("fail", failCheckBox.isChecked());
				SampleFirebaseJobService.runIntentInService(bundle);
			}
		});
		findView(R.id.commandService1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putString("a", "3");
				bundle.putBoolean("fail", failCheckBox.isChecked());
				ServiceCommand serviceCommand = new SampleServiceCommand1();
				serviceCommand.start(bundle);
			}
		});
		findView(R.id.commandService2).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putString("a", "4");
				bundle.putBoolean("fail", failCheckBox.isChecked());
				ServiceCommand serviceCommand = new SampleServiceCommand2();
				serviceCommand.start(bundle);
			}
		});
		findView(R.id.commandService3).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putString("a", "5");
				bundle.putBoolean("fail", failCheckBox.isChecked());
				ServiceCommand serviceCommand = new SampleServiceCommand3();
				serviceCommand.start(bundle);
			}
		});
		findView(R.id.cancelAllJobs).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getActivity()));
				dispatcher.cancelAll();
			}
		});
	}
}
