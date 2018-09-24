package com.jdroid.android.shortcuts;

import androidx.annotation.NonNull;

import com.jdroid.android.application.AbstractAppModule;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.shortcuts.analytics.AppShortcutsAnalyticsSender;
import com.jdroid.android.shortcuts.analytics.AppShortcutsAnalyticsTracker;
import com.jdroid.android.shortcuts.analytics.FirebaseAppShortcutsAnalyticsTracker;
import com.jdroid.java.analytics.AnalyticsSender;
import com.jdroid.java.analytics.AnalyticsTracker;
import com.jdroid.java.collections.Lists;

import java.util.List;

public class AppShortcutsAppModule extends AbstractAppModule {

	public static final String MODULE_NAME = AppShortcutsAppModule.class.getName();

	public static AppShortcutsAppModule get() {
		return (AppShortcutsAppModule)AbstractApplication.get().getAppModule(MODULE_NAME);
	}

	@NonNull
	@Override
	public AnalyticsSender<? extends AnalyticsTracker> createModuleAnalyticsSender(List<? extends AnalyticsTracker> analyticsTrackers) {
		return new AppShortcutsAnalyticsSender((List<AppShortcutsAnalyticsTracker>)analyticsTrackers);
	}

	@Override
	public List<? extends AnalyticsTracker> createModuleAnalyticsTrackers() {
		return Lists.newArrayList(new FirebaseAppShortcutsAnalyticsTracker());
	}

	@NonNull
	@Override
	public AppShortcutsAnalyticsSender getModuleAnalyticsSender() {
		return (AppShortcutsAnalyticsSender)super.getModuleAnalyticsSender();
	}
}