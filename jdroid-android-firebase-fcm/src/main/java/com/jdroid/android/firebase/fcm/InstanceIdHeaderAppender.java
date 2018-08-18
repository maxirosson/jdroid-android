package com.jdroid.android.firebase.fcm;

import com.jdroid.java.http.HttpResponseWrapper;
import com.jdroid.java.http.HttpService;
import com.jdroid.java.http.HttpServiceProcessor;

public class InstanceIdHeaderAppender implements HttpServiceProcessor {

	public static final String INSTANCE_ID_HEADER = "instanceId";

	private static final InstanceIdHeaderAppender INSTANCE = new InstanceIdHeaderAppender();

	public static InstanceIdHeaderAppender get() {
		return INSTANCE;
	}

	@Override
	public void onInit(HttpService httpService) {
		// Do Nothing
	}

	@Override
	public void beforeExecute(HttpService httpService) {
		httpService.addHeader(INSTANCE_ID_HEADER, InstanceIdHelper.getInstanceId());
	}

	@Override
	public void afterExecute(HttpService httpService, HttpResponseWrapper httpResponse) {
		// Do Nothing
	}

}
