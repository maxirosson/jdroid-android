package com.jdroid.android.firebase.admob;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.activity.ActivityDelegate;
import com.jdroid.android.application.AbstractAppModule;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.fragment.FragmentDelegate;
import com.jdroid.android.firebase.admob.analytics.AdMobAnalyticsSender;
import com.jdroid.android.firebase.admob.analytics.AdMobAnalyticsTracker;
import com.jdroid.android.firebase.admob.analytics.GoogleAdMobAnalyticsTracker;
import com.jdroid.java.analytics.AnalyticsSender;
import com.jdroid.java.analytics.AnalyticsTracker;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.remoteconfig.RemoteConfigParameter;

import java.util.Arrays;
import java.util.List;

public class AdMobAppModule extends AbstractAppModule {

	public static final String MODULE_NAME = AdMobAppModule.class.getName();

	public static AdMobAppModule get() {
		return (AdMobAppModule)AbstractApplication.get().getAppModule(MODULE_NAME);
	}
	
	private static AdMobAppContext adMobAppContext = new AdMobAppContext();

	public static AdMobAppContext getAdMobAppContext() {
		return adMobAppContext;
	}
	
	public static void setAdMobAppContext(AdMobAppContext adMobAppContext) {
		AdMobAppModule.adMobAppContext = adMobAppContext;
	}

	@Override
	public ActivityDelegate createActivityDelegate(AbstractFragmentActivity abstractFragmentActivity) {
		return new AdMobActivityDelegate(abstractFragmentActivity);
	}

	@Override
	public FragmentDelegate createFragmentDelegate(Fragment fragment) {
		return null;
	}

	@NonNull
	@Override
	public AnalyticsSender<? extends AnalyticsTracker> createModuleAnalyticsSender(List<? extends AnalyticsTracker> analyticsTrackers) {
		return new AdMobAnalyticsSender((List<AdMobAnalyticsTracker>)analyticsTrackers);
	}

	@Override
	public List<? extends AnalyticsTracker> createModuleAnalyticsTrackers() {
		return Lists.newArrayList(new GoogleAdMobAnalyticsTracker());
	}

	@NonNull
	@Override
	public AdMobAnalyticsSender getModuleAnalyticsSender() {
		return (AdMobAnalyticsSender)super.getModuleAnalyticsSender();
	}
	
	@NonNull
	@Override
	public List<RemoteConfigParameter> getRemoteConfigParameters() {
		return Arrays.asList(AdMobRemoteConfigParameter.values());
	}
}