package com.jdroid.android.about;

import com.jdroid.android.about.analytics.AboutAnalyticsSender;
import com.jdroid.android.about.analytics.AboutAnalyticsTracker;
import com.jdroid.android.about.analytics.FirebaseAboutAnalyticsTracker;
import com.jdroid.android.application.AbstractAppModule;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.analytics.AnalyticsSender;
import com.jdroid.java.analytics.AnalyticsTracker;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.remoteconfig.RemoteConfigParameter;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;

public class AboutAppModule extends AbstractAppModule {

	public static final String MODULE_NAME = AboutAppModule.class.getName();

	public static AboutAppModule get() {
		return (AboutAppModule)AbstractApplication.get().getAppModule(MODULE_NAME);
	}

	private AboutContext aboutContext;

	public AboutAppModule() {
		aboutContext = createAboutContext();
	}

	protected AboutContext createAboutContext() {
		return new AboutContext();
	}

	public AboutContext getAboutContext() {
		return aboutContext;
	}

	@NonNull
	@Override
	public AnalyticsSender<? extends AnalyticsTracker> createModuleAnalyticsSender(List<? extends AnalyticsTracker> analyticsTrackers) {
		return new AboutAnalyticsSender((List<AboutAnalyticsTracker>)analyticsTrackers);
	}

	@Override
	public List<? extends AnalyticsTracker> createModuleAnalyticsTrackers() {
		return Lists.newArrayList(new FirebaseAboutAnalyticsTracker());
	}

	@NonNull
	@Override
	public AboutAnalyticsSender getModuleAnalyticsSender() {
		return (AboutAnalyticsSender)super.getModuleAnalyticsSender();
	}

	@NonNull
	@Override
	public List<RemoteConfigParameter> getRemoteConfigParameters() {
		return Arrays.asList(AboutRemoteConfigParameter.values());
	}
}