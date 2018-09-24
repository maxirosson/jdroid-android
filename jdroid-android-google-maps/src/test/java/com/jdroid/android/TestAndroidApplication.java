package com.jdroid.android;

import android.app.Activity;
import androidx.annotation.NonNull;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.context.AppContext;
import com.jdroid.android.context.BuildConfigUtils;
import com.jdroid.android.debug.DebugContext;
import com.jdroid.android.http.HttpConfiguration;
import com.jdroid.android.lifecycle.ApplicationLifecycleCallback;
import com.jdroid.android.lifecycle.ApplicationLifecycleHelper;
import com.jdroid.java.http.okhttp.OkHttpServiceFactory;
import com.jdroid.java.utils.ReflectionUtils;

import java.util.ArrayList;
import java.util.List;

public class TestAndroidApplication extends AbstractApplication {

	public TestAndroidApplication() {
		HttpConfiguration.setHttpServiceFactory(new OkHttpServiceFactory());
	}

	@Override
	protected void onInitMultiDex() {
		// Multidex support doesn't play well with Robolectric yet

		BuildConfigUtils.setBuildConfigResolver(new com.jdroid.android.TestBuildConfigResolver());
		ReflectionUtils.setStaticField(ApplicationLifecycleHelper.class, "applicationLifecycleCallbacks", createApplicationLifecycleCallbacks());
	}

	/**
	 * This method can be overridden in subclasses to provide the list of ApplicationLifecycleCallback to use in the tests.
	 *
	 * @return a list of ApplicationLifecycleCallback to use in the tests, or empty list.
	 */
	protected List<ApplicationLifecycleCallback> createApplicationLifecycleCallbacks() {
		return new ArrayList<>();
	}

	@Override
	protected Boolean isMultiProcessSupportEnabled() {
		return false;
	}

	@Override
	public Class<? extends Activity> getHomeActivityClass() {
		return null;
	}

	@NonNull
	@Override
	protected AppContext createAppContext() {
		return new TestAppContext();
	}

	@Override
	protected DebugContext createDebugContext() {
		return new TestDebugContext();
	}

	@Override
	public int getLauncherIconResId() {
		return 0;
	}

	@Override
	public int getNotificationIconResId() {
		return 0;
	}

	@Override
	public String getManifestPackageName() {
		return null;
	}

	@Override
	public void initExceptionHandlers() {
		Thread.setDefaultUncaughtExceptionHandler(new TestExceptionHandler());
	}

	@Override
	protected void verifyAppLaunchStatus() {
		// Do Nothing
	}
}

