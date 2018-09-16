package com.jdroid.android.lifecycle;

public class ApplicationLifecycleInitProvider extends AbstractInitProvider {

	@Override
	protected void init() {
		ApplicationLifecycleHelper.onProviderInit(getContext());
	}
}
