package com.jdroid.android.firebase.remoteconfig;

import android.content.Context;
import android.os.Bundle;

import com.jdroid.android.firebase.jobdispatcher.ServiceCommand;

public class FirebaseRemoteConfigFetchCommand extends ServiceCommand {

	private static final String SET_EXPERIMENT_USER_PROPERTY = "setExperimentUserProperty";

	public void start() {
		start( false);
	}

	public void start(Boolean setExperimentUserProperty) {
		Bundle bundle = new Bundle();
		bundle.putBoolean(FirebaseRemoteConfigFetchCommand.SET_EXPERIMENT_USER_PROPERTY, setExperimentUserProperty);
		start(bundle);
	}

	@Override
	protected boolean execute(Context context, Bundle bundle) {
		Boolean setExperimentUserProperty = bundle.getBoolean(SET_EXPERIMENT_USER_PROPERTY);
		FirebaseRemoteConfigLoader.get().fetch(setExperimentUserProperty, null);
		return false;
	}
}
