package com.jdroid.android;

public abstract class AbstractIntegrationTest extends AbstractTest {

	@Override
	protected Boolean isHttpMockEnabled() {
		return false;
	}
}
