package com.jdroid.android.sample.ui.google.gcm;

import android.os.Bundle;
import android.view.View;

import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.google.gcm.GcmRegistrationCommand;
import com.jdroid.android.google.instanceid.InstanceIdHelper;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.api.SampleApiService;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.exception.UnexpectedException;

import java.io.IOException;

import static javafx.scene.input.KeyCode.R;

public class GcmFragment extends AbstractFragment {

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.gcm_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		findView(R.id.registerDevice).setOnClickListener(v -> new GcmRegistrationCommand().start(false));
		findView(R.id.registerDeviceAndUpdateLastActiveTimestamp).setOnClickListener(v -> new GcmRegistrationCommand().start(true));

		findView(R.id.removeInstanceId).setOnClickListener(v ->
				ExecutorUtils.execute(() -> InstanceIdHelper.removeInstanceId()));

		findView(R.id.removeDevice).setOnClickListener(v ->
				ExecutorUtils.execute(() -> new SampleApiService().removeDevice()));

		findView(R.id.sendPush).setOnClickListener(v ->
				ExecutorUtils.execute(() -> {
						String registrationToken = null;
						try {
						registrationToken = GcmRegistrationCommand.getRegistrationToken(GcmFragment.this.getActivity());
						new SampleApiService().sendPush(registrationToken);
						} catch (IOException e) {
						throw new UnexpectedException(e);
						}
					}
				));
	}
}
