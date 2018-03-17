package com.jdroid.android.debug;

import android.support.v4.util.Pair;

import com.jdroid.android.activity.ActivityLauncher;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.debug.appenders.DatabaseDebugPrefsAppender;
import com.jdroid.android.debug.appenders.ExceptionHandlingDebugPrefsAppender;
import com.jdroid.android.debug.appenders.HttpCacheDebugPrefsAppender;
import com.jdroid.android.debug.appenders.HttpMocksDebugPrefsAppender;
import com.jdroid.android.debug.appenders.InfoDebugPrefsAppender;
import com.jdroid.android.debug.appenders.LogsDebugPrefsAppender;
import com.jdroid.android.debug.appenders.NavDrawerDebugPrefsAppender;
import com.jdroid.android.debug.appenders.NotificationsDebugPrefsAppender;
import com.jdroid.android.debug.appenders.ServersDebugPrefsAppender;
import com.jdroid.android.debug.appenders.UriMapperPrefsAppender;
import com.jdroid.android.debug.appenders.UsageStatsDebugPrefsAppender;
import com.jdroid.android.log.DatabaseLog;
import com.jdroid.android.log.DatabaseLogsRepository;
import com.jdroid.android.sqlite.SQLiteHelper;
import com.jdroid.android.utils.AppUtils;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.collections.Maps;
import com.jdroid.java.domain.Identifiable;
import com.jdroid.java.http.Server;
import com.jdroid.java.repository.Repository;

import java.util.List;
import java.util.Map;

public class DebugContext {

	private List<Pair<String, Object>> customDebugInfoProperties = Lists.newArrayList();

	public void launchActivityDebugSettingsActivity() {
		ActivityLauncher.launchActivity(DebugSettingsActivity.class);
	}

	public void initDebugRepositories(
			Map<Class<? extends Identifiable>, Repository<? extends Identifiable>> repositories, SQLiteHelper dbHelper) {
		if (AbstractApplication.get().isDebugLogRepositoryEnabled() && !AppUtils.isReleaseBuildType()) {
			repositories.put(DatabaseLog.class, new DatabaseLogsRepository(dbHelper));
		}
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
	
	public DatabaseDebugPrefsAppender createDatabaseDebugPrefsAppender() {
		return new DatabaseDebugPrefsAppender();
	}
	
	public LogsDebugPrefsAppender createLogsDebugPrefsAppender() {
		return new LogsDebugPrefsAppender();
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
}
