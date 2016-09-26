package com.jdroid.android.crashlytics;

import android.support.v4.util.Pair;

import com.crashlytics.android.Crashlytics;
import com.jdroid.android.analytics.AnalyticsTracker;
import com.jdroid.android.application.AbstractAppModule;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.collections.Lists;

import java.util.List;

import io.fabric.sdk.android.Kit;

public class CrashlyticsAppModule extends AbstractAppModule {

	public static final String MODULE_NAME = CrashlyticsAppModule.class.getName();

	private CrashlyticsAppContext crashlyticsAppContext;

	public static CrashlyticsAppModule get() {
		return (CrashlyticsAppModule)AbstractApplication.get().getAppModule(MODULE_NAME);
	}

	public CrashlyticsAppModule() {
		crashlyticsAppContext = createCrashlyticsAppContext();
	}

	protected CrashlyticsAppContext createCrashlyticsAppContext() {
		return new CrashlyticsAppContext();
	}

	public CrashlyticsAppContext getCrashlyticsAppContext() {
		return crashlyticsAppContext;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		AbstractApplication.get().getDebugContext().addCustomDebugInfoProperty(new Pair<String, Object>("Crashlytics Enabled", crashlyticsAppContext.isCrashlyticsEnabled()));
	}

	@Override
	public List<Kit> getFabricKits() {
		return Lists.<Kit>newArrayList(new Crashlytics());
	}

	@Override
	public List<? extends AnalyticsTracker> getAnalyticsTrackers() {
		return Lists.newArrayList(crashlyticsAppContext.getAnalyticsTracker());
	}
}
