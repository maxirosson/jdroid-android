package com.jdroid.android;

import com.jdroid.android.debug.DebugContext;

public class TestDebugContext extends DebugContext {

	public static final Boolean FAKE_HTTP_ENABLED = true;

	@Override
	public Boolean isHttpMockEnabled() {
		return FAKE_HTTP_ENABLED;
	}
}
