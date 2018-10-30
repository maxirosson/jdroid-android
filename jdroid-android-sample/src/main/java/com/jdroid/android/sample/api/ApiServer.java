package com.jdroid.android.sample.api;

import android.os.Bundle;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.firebase.fcm.FcmSender;
import com.jdroid.android.firebase.fcm.instanceid.InstanceIdHeaderAppender;
import com.jdroid.android.firebase.fcm.device.Device;
import com.jdroid.android.http.DefaultHeaderAppender;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.http.HttpServiceProcessor;
import com.jdroid.java.http.Server;

import java.util.List;

public enum ApiServer implements FcmSender {

	PROD("url", ":8080", "/api", true, true) {
		@Override
		public String getSenderId() {
			// TODO Return production sender id here
			return null;
		}
	},
	UAT("staging-jdroid.rhcloud.com", null, "/api", true, false),
	DEV(null, ":8080", "/jdroid-java-webapp-sample/api", true, false),
	MOCKEY_LOCAL(null, ":8081", "/service/api", false, false);

	private String domain;
	private String port;
	private String segment;
	private Boolean supportsSsl;
	private Boolean isProduction;

	ApiServer(String domain, String port, String segment, Boolean supportsSsl, Boolean isProduction) {
		this.domain = domain;
		this.port = port;
		this.segment = segment;
		this.supportsSsl = supportsSsl;
		this.isProduction = isProduction;
	}

	@Override
	public String getName() {
		return name();
	}

	@Override
	public String getBaseUrl() {
		if (domain == null) {
			return AbstractApplication.get().getAppContext().getLocalIp() + port + segment;
		} else {
			StringBuilder urlBuilder = new StringBuilder();
			urlBuilder.append(domain);
			if (port != null) {
				urlBuilder.append(port);
			}
			if (segment != null) {
				urlBuilder.append(segment);
			}
			return urlBuilder.toString();
		}
	}

	@Override
	public Boolean supportsSsl() {
		return supportsSsl;
	}

	@Override
	public Boolean isProduction() {
		return isProduction;
	}

	@Override
	public List<HttpServiceProcessor> getHttpServiceProcessors() {
		return Lists.newArrayList(DefaultHeaderAppender.get(), InstanceIdHeaderAppender.get(), HttpResponseValidator.get());
	}

	@Override
	public Server instance(String name) {
		return valueOf(name);
	}

	@Override
	public String getSenderId() {
		return "527857945512";
	}

	@Override
	public void onRegisterOnServer(String registrationToken, Boolean updateLastActiveTimestamp, Bundle bundle) {
		SampleApiService service = new SampleApiService();

		Device device = new Device(registrationToken, null);
		service.addDevice(device, updateLastActiveTimestamp);
	}
}