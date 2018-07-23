package com.jdroid.android.sample.ui.leakcanary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.service.AbstractWorkerService;

public class LeakCanaryActivity extends FragmentContainerActivity {
	
	public static LeakCanaryActivity LEAK;
	
	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return LeakCanaryFragment.class;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		LEAK = this;
		
		AbstractWorkerService.runIntentInService(AbstractApplication.get(), new Intent(), LeakCanaryService.class);
	}
}
