package com.jdroid.android.google.admob;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.activity.ActivityDelegate;
import com.jdroid.android.application.AbstractAppModule;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.debug.PreferencesAppender;
import com.jdroid.android.fragment.FragmentDelegate;
import com.jdroid.android.google.admob.analytics.AdMobAnalyticsSender;
import com.jdroid.android.google.admob.analytics.AdMobAnalyticsTracker;
import com.jdroid.android.google.admob.analytics.AdMobGoogleAnalyticsTracker;
import com.jdroid.java.analytics.BaseAnalyticsSender;
import com.jdroid.java.analytics.BaseAnalyticsTracker;
import com.jdroid.java.collections.Lists;

import java.util.List;

public class AdMobAppModule extends AbstractAppModule {

	public static final String MODULE_NAME = AdMobAppModule.class.getName();

	private AdMobAppContext adMobAppContext;
	private AdMobDebugContext adMobDebugContext;

	public AdMobAppModule() {
		adMobAppContext = createAdMobAppContext();
	}

	public static AdMobAppModule get() {
		return (AdMobAppModule)AbstractApplication.get().getAppModule(MODULE_NAME);
	}

	protected AdMobAppContext createAdMobAppContext() {
		return new AdMobAppContext();
	}

	public AdMobAppContext getAdMobAppContext() {
		return adMobAppContext;
	}

	@Override
	public List<PreferencesAppender> getPreferencesAppenders() {
		return getGcmDebugContext().getPreferencesAppenders();
	}

	public AdMobDebugContext getGcmDebugContext() {
		synchronized (AbstractApplication.class) {
			if (adMobDebugContext == null) {
				adMobDebugContext = createAdMobDebugContext();
			}
		}
		return adMobDebugContext;
	}

	protected AdMobDebugContext createAdMobDebugContext() {
		return new AdMobDebugContext();
	}

	@Override
	public ActivityDelegate createActivityDelegate(AbstractFragmentActivity abstractFragmentActivity) {
		return new AdMobActivityDelegate(abstractFragmentActivity);
	}

	@Override
	public FragmentDelegate createFragmentDelegate(Fragment fragment) {
		return new AdMobFragmentDelegate(fragment);
	}

	@NonNull
	@Override
	public BaseAnalyticsSender<? extends BaseAnalyticsTracker> createModuleAnalyticsSender(List<? extends BaseAnalyticsTracker> analyticsTrackers) {
		return new AdMobAnalyticsSender((List<AdMobAnalyticsTracker>)analyticsTrackers);
	}

	@Override
	public List<? extends BaseAnalyticsTracker> createModuleAnalyticsTrackers() {
		return Lists.newArrayList(new AdMobGoogleAnalyticsTracker());
	}

	@NonNull
	@Override
	public AdMobAnalyticsSender getAnalyticsSender() {
		return (AdMobAnalyticsSender)super.getAnalyticsSender();
	}
}