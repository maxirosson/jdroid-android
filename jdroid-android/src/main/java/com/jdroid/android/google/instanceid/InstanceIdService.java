package com.jdroid.android.google.instanceid;

import com.google.android.gms.iid.InstanceIDListenerService;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.application.AppModule;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

public class InstanceIdService extends InstanceIDListenerService {

	private static final Logger LOGGER = LoggerUtils.getLogger(InstanceIdService.class);

	/**
	 * Called if InstanceID token is updated. This may occur if the security of
	 * the previous token had been compromised. This call is initiated by the
	 * InstanceID provider.
	 */
	@Override
	public void onTokenRefresh() {
		LOGGER.info("Refreshing Instance Id Tokens");
		InstanceIdHelper.clearInstanceId();

		for (AppModule appModule : AbstractApplication.get().getAppModules()) {
			appModule.onInstanceIdTokenRefresh();
		}
	}
};