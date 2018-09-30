package com.jdroid.android.firebase.fcm.remoteconfig;

import android.content.Context;
import android.os.Bundle;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.firebase.jobdispatcher.ServiceCommand;

public class RemoteConfigFetchCommand extends ServiceCommand {

	@Override
	protected boolean execute(Context context, Bundle bundle) {
		AbstractApplication.get().getRemoteConfigLoader().fetch();
		return false;
	}
}
