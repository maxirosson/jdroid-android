package com.jdroid.android;

public abstract class AbstractUnitTest extends AbstractTest {

	@Override
	protected Boolean isHttpMockEnabled() {
		return true;
	}
}
