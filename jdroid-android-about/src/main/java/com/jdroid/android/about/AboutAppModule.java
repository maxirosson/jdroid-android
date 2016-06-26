package com.jdroid.android.about;

import android.support.annotation.NonNull;

import com.jdroid.android.about.analytics.AboutAnalyticsSender;
import com.jdroid.android.about.analytics.AboutAnalyticsTracker;
import com.jdroid.android.about.analytics.AboutGoogleAnalyticsTracker;
import com.jdroid.android.application.AbstractAppModule;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.debug.PreferencesAppender;
import com.jdroid.java.analytics.BaseAnalyticsSender;
import com.jdroid.java.analytics.BaseAnalyticsTracker;
import com.jdroid.java.collections.Lists;

import java.util.List;

public class AboutAppModule extends AbstractAppModule {

	public static final String MODULE_NAME = AboutAppModule.class.getName();

	private AboutContext aboutContext;
	private AboutDebugContext aboutDebugContext;

	public AboutAppModule() {
		aboutContext = createAboutContext();
	}

	public static AboutAppModule get() {
		return (AboutAppModule)AbstractApplication.get().getAppModule(MODULE_NAME);
	}

	protected AboutContext createAboutContext() {
		return new AboutContext();
	}

	public AboutContext getAboutContext() {
		return aboutContext;
	}

	@Override
	public List<PreferencesAppender> getPreferencesAppenders() {
		return getGcmDebugContext().getPreferencesAppenders();
	}

	public AboutDebugContext getGcmDebugContext() {
		synchronized (AbstractApplication.class) {
			if (aboutDebugContext == null) {
				aboutDebugContext = createAboutDebugContext();
			}
		}
		return aboutDebugContext;
	}

	protected AboutDebugContext createAboutDebugContext() {
		return new AboutDebugContext();
	}

	@NonNull
	@Override
	public BaseAnalyticsSender<? extends BaseAnalyticsTracker> createModuleAnalyticsSender(List<? extends BaseAnalyticsTracker> analyticsTrackers) {
		return new AboutAnalyticsSender((List<AboutAnalyticsTracker>)analyticsTrackers);
	}

	@Override
	public List<? extends BaseAnalyticsTracker> createModuleAnalyticsTrackers() {
		return Lists.newArrayList(new AboutGoogleAnalyticsTracker());
	}

	@NonNull
	@Override
	public AboutAnalyticsSender getAnalyticsSender() {
		return (AboutAnalyticsSender)super.getAnalyticsSender();
	}
}