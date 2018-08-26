package com.jdroid.android.debug;

import android.app.Activity;
import android.support.v4.util.Pair;

import com.jdroid.android.activity.ActivityLauncher;
import com.jdroid.android.debug.appenders.ExceptionHandlingDebugPrefsAppender;
import com.jdroid.android.debug.appenders.HttpCacheDebugPrefsAppender;
import com.jdroid.android.debug.appenders.HttpMocksDebugPrefsAppender;
import com.jdroid.android.debug.appenders.InfoDebugPrefsAppender;
import com.jdroid.android.debug.appenders.NavDrawerDebugPrefsAppender;
import com.jdroid.android.debug.appenders.NotificationsDebugPrefsAppender;
import com.jdroid.android.debug.appenders.ServersDebugPrefsAppender;
import com.jdroid.android.debug.appenders.UriMapperPrefsAppender;
import com.jdroid.android.debug.appenders.UsageStatsDebugPrefsAppender;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.collections.Maps;
import com.jdroid.java.http.Server;
import com.jdroid.java.remoteconfig.RemoteConfigParameter;

import java.util.List;
import java.util.Map;

public class DebugContext {

	private List<Pair<String, Object>> customDebugInfoProperties = Lists.newArrayList();

	public void launchActivityDebugSettingsActivity(Activity activity) {
		ActivityLauncher.startActivity(activity, DebugSettingsActivity.class);
	}

	public List<Pair<String, Object>> getCustomDebugInfoProperties() {
		return customDebugInfoProperties;
	}

	public void addCustomDebugInfoProperty(Pair<String, Object> pair) {
		customDebugInfoProperties.add(pair);
	}

	public ServersDebugPrefsAppender createServersDebugPrefsAppender() {
		return new ServersDebugPrefsAppender(getServersMap());
	}

	public Map<Class<? extends Server>, List<? extends Server>> getServersMap() {
		return Maps.newHashMap();
	}

	public ExceptionHandlingDebugPrefsAppender createExceptionHandlingDebugPrefsAppender() {
		return new ExceptionHandlingDebugPrefsAppender();
	}

	public HttpCacheDebugPrefsAppender createHttpCacheDebugPrefsAppender() {
		return new HttpCacheDebugPrefsAppender();
	}

	public NavDrawerDebugPrefsAppender createNavDrawerDebugPrefsAppender() {
		return new NavDrawerDebugPrefsAppender();
	}

	public HttpMocksDebugPrefsAppender createHttpMocksDebugPrefsAppender() {
		return new HttpMocksDebugPrefsAppender();
	}

	public InfoDebugPrefsAppender createInfoDebugPrefsAppender() {
		return new InfoDebugPrefsAppender();
	}

	public UsageStatsDebugPrefsAppender createUsageStatsDebugPrefsAppender() {
		return new UsageStatsDebugPrefsAppender();
	}

	public UriMapperPrefsAppender createUriMapperPrefsAppender() {
		return new UriMapperPrefsAppender();
	}

	public NotificationsDebugPrefsAppender createNotificationsDebugPrefsAppender() {
		return new NotificationsDebugPrefsAppender();
	}

	public List<PreferencesAppender> getCustomPreferencesAppenders() {
		return Lists.newArrayList();
	}

	public List<String> getUrlsToTest() {
		return Lists.newArrayList();
	}

	public List<RemoteConfigParameter> getRemoteConfigParameters() {
		return Lists.newArrayList();
	}
}
