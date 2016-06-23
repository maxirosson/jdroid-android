package com.jdroid.android.sample;

import com.jdroid.android.context.AppContext;

public class TestAppContext extends AppContext {

	@Override
	protected String getServerName() {
		return null;
	}

	@Override
	public String getBuildType() {
		return "test";
	}

	@Override
	public Boolean isGoogleAnalyticsEnabled() {
		return false;
	}

	@Override
	public String getGoogleAnalyticsTrackingId() {
		return null;
	}

	@Override
	public Boolean isGoogleAnalyticsDebugEnabled() {
		return false;
	}

	@Override
	public String getLocalIp() {
		return null;
	}

	@Override
	public Boolean isStrictModeEnabled() {
		return false;
	}
}
