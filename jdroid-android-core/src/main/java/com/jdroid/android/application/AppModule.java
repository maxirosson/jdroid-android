package com.jdroid.android.application;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.activity.ActivityDelegate;
import com.jdroid.android.fragment.FragmentDelegate;
import com.jdroid.java.analytics.AnalyticsSender;
import com.jdroid.java.analytics.AnalyticsTracker;
import com.jdroid.java.remoteconfig.RemoteConfigParameter;

import java.util.List;

public interface AppModule {

	@MainThread
	public void onGooglePlayServicesUpdated();

	@MainThread
	public ActivityDelegate createActivityDelegate(AbstractFragmentActivity abstractFragmentActivity);

	@MainThread
	public FragmentDelegate createFragmentDelegate(Fragment fragment);

	@NonNull
	public List<RemoteConfigParameter> getRemoteConfigParameters();

	// Module Analytics

	public AnalyticsSender<? extends AnalyticsTracker> createModuleAnalyticsSender(List<? extends AnalyticsTracker> analyticsTrackers);

	public List<? extends AnalyticsTracker> createModuleAnalyticsTrackers();

	public AnalyticsSender<? extends AnalyticsTracker> getModuleAnalyticsSender();

}
