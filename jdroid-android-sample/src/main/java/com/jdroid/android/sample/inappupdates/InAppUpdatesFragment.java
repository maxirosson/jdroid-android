package com.jdroid.android.sample.inappupdates;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.View;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnFailureListener;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.sample.R;
import com.jdroid.android.utils.ToastUtils;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

public class InAppUpdatesFragment extends AbstractFragment {

	private final static Logger LOGGER = LoggerUtils.getLogger(InAppUpdatesFragment.class);

	private static final int IN_APP_UPDATE_REQUEST_CODE = 1;

	@Override
	public Integer getContentFragmentLayout() {
		return R.layout.inapp_updates_fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		findView(R.id.inAppUpdateFlexible).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(getContext());
				Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
				appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
					@Override
					public void onSuccess(AppUpdateInfo appUpdateInfo) {
						// Checks that the platform will allow the specified type of update.
						if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
							try {
								appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.FLEXIBLE, getActivity(), IN_APP_UPDATE_REQUEST_CODE);
							} catch (IntentSender.SendIntentException e) {
								AbstractApplication.get().getExceptionHandler().logHandledException(e);
								ToastUtils.showToast(e.getMessage());
							}
						} else {
							LOGGER.warn("Flexible in-app update not available");
						}
					}
				});
				appUpdateInfoTask.addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(Exception e) {
						AbstractApplication.get().getExceptionHandler().logHandledException(e);
						ToastUtils.showToast(e.getMessage());
					}
				});
			}
		});

		findView(R.id.inAppUpdateImmediate).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(getContext());
				Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
				appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
					@Override
					public void onSuccess(AppUpdateInfo appUpdateInfo) {
						// Checks that the platform will allow the specified type of update.
						if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
							try {
								appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, getActivity(), IN_APP_UPDATE_REQUEST_CODE);
							} catch (IntentSender.SendIntentException e) {
								AbstractApplication.get().getExceptionHandler().logHandledException(e);
								ToastUtils.showToast(e.getMessage());
							}
						} else {
							LOGGER.warn("Immediate in-app update not available");
						}
					}
				});
				appUpdateInfoTask.addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(Exception e) {
						AbstractApplication.get().getExceptionHandler().logHandledException(e);
						ToastUtils.showToast(e.getMessage());
					}
				});
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == IN_APP_UPDATE_REQUEST_CODE) {
			if (resultCode != Activity.RESULT_OK) {
				AbstractApplication.get().getExceptionHandler().logHandledException("Update flow failed! Result code: " + resultCode);
				// If the update is cancelled or fails, you can request to start the update again.
			}
		}
	}
}
