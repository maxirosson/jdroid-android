package com.jdroid.android.application;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.support.annotation.CallSuper;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.support.v4.app.Fragment;

import com.jdroid.android.BuildConfig;
import com.jdroid.android.R;
import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.activity.ActivityHelper;
import com.jdroid.android.activity.ActivityLifecycleHandler;
import com.jdroid.android.analytics.CoreAnalyticsSender;
import com.jdroid.android.analytics.CoreAnalyticsTracker;
import com.jdroid.android.context.AndroidGitContext;
import com.jdroid.android.context.AppContext;
import com.jdroid.android.debug.DebugContext;
import com.jdroid.android.exception.DefaultExceptionHandler;
import com.jdroid.android.exception.ExceptionHandler;
import com.jdroid.android.firebase.testlab.FirebaseTestLab;
import com.jdroid.android.fragment.FragmentHelper;
import com.jdroid.android.http.cache.CacheManager;
import com.jdroid.android.leakcanary.LeakCanaryHelper;
import com.jdroid.android.lifecycle.ApplicationLifecycleHelper;
import com.jdroid.android.notification.NotificationChannelType;
import com.jdroid.android.notification.NotificationUtils;
import com.jdroid.android.repository.UserRepository;
import com.jdroid.android.sqlite.SQLiteHelper;
import com.jdroid.android.sqlite.SQLiteUpgradeStep;
import com.jdroid.android.strictmode.StrictModeHelper;
import com.jdroid.android.uri.UriMapper;
import com.jdroid.android.utils.AppUtils;
import com.jdroid.android.utils.DeviceUtils;
import com.jdroid.android.utils.ProcessUtils;
import com.jdroid.android.utils.SharedPreferencesHelper;
import com.jdroid.android.utils.ToastUtils;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.collections.Maps;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.context.GitContext;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.domain.Identifiable;
import com.jdroid.java.remoteconfig.RemoteConfigLoader;
import com.jdroid.java.repository.Repository;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.ReflectionUtils;
import com.jdroid.java.utils.StringUtils;

import org.slf4j.Logger;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractApplication extends Application {

	/**
	 * The LOGGER variable is initialized in the "OnCreate" method, after that "LoggerUtils" has been properly
	 * configured by the superclass.
	 */
	protected static Logger LOGGER;

	private static final String INSTALLATION_SOURCE = "installationSource";
	private static final String VERSION_CODE_KEY = "versionCodeKey";

	protected static AbstractApplication INSTANCE;

	private AppContext appContext;
	private GitContext gitContext;
	private DebugContext debugContext;

	private List<CoreAnalyticsTracker> coreAnalyticsTrackers = Lists.newArrayList();
	private CoreAnalyticsSender<? extends CoreAnalyticsTracker> coreAnalyticsSender;
	private UriMapper uriMapper;

	private AppLaunchStatus appLaunchStatus;

	private Map<Class<? extends Identifiable>, Repository<? extends Identifiable>> repositories;

	private Map<String, AppModule> appModulesMap = Maps.newLinkedHashMap();

	private UpdateManager updateManager;
	private CacheManager cacheManager;

	private String installationSource;

	private UncaughtExceptionHandler defaultAndroidExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

	private ActivityLifecycleHandler activityLifecycleHandler;

	private RemoteConfigLoader remoteConfigLoader;

	public AbstractApplication() {
		INSTANCE = this;
	}

	public static AbstractApplication get() {
		return INSTANCE;
	}

	private void initLogging() {
		if (LOGGER == null) {
			LoggerUtils.setEnabled(isLoggingEnabled());
			LOGGER = LoggerUtils.getLogger(AbstractApplication.class);
		}
	}

	@MainThread
	@CallSuper
	@Override
	protected final void attachBaseContext(Context base) {
		super.attachBaseContext(base);

		onInitMultiDex();

		if (!isMultiProcessSupportEnabled() || ProcessUtils.isMainProcess(this)) {
			initLogging();
			ApplicationLifecycleHelper.attachBaseContext(this);
			onMainProcessAttachBaseContext();
		} else {
			onSecondaryProcessAttachBaseContext(ProcessUtils.getProcessInfo(this));
		}
	}

	@MainThread
	protected void onInitMultiDex() {
		// Do nothing
	}

	@MainThread
	protected void onMainProcessAttachBaseContext() {
		// Do nothing
	}

	@MainThread
	protected void onSecondaryProcessAttachBaseContext(ActivityManager.RunningAppProcessInfo processInfo) {
		// Do nothing
	}

	@MainThread
	public void onProviderInit() {
		// Do nothing
	}

	@MainThread
	@CallSuper
	@Override
	public final void onCreate() {

		if (!isMultiProcessSupportEnabled() || ProcessUtils.isMainProcess(this)) {
			initStrictMode();
		}

		super.onCreate();

		if (!isMultiProcessSupportEnabled() || ProcessUtils.isMainProcess(this)) {
			ApplicationLifecycleHelper.onCreate(this);

			appContext = createAppContext();

			NotificationUtils.createNotificationChannelsByType(getNotificationChannelTypes());

			initAppModule(appModulesMap);

			initCoreAnalyticsSender();

			uriMapper = createUriMapper();

			updateManager = new UpdateManager();
			updateManager.addUpdateSteps(createUpdateSteps());

			initExceptionHandlers();
			LoggerUtils.setExceptionLogger(getExceptionHandler());

			// This is required to initialize the statics fields of the utils classes.
			ToastUtils.init();
			DateUtils.init();

			initRepositories();

			ExecutorUtils.execute(new Runnable() {

				@Override
				public void run() {
					DeviceUtils.getDeviceYearClass();
					fetchInstallationSource();
					verifyAppLaunchStatus();

					if (getCacheManager() != null) {
						getCacheManager().initFileSystemCache();
					}
				}
			});

			activityLifecycleHandler = new ActivityLifecycleHandler();
			registerActivityLifecycleCallbacks(activityLifecycleHandler);

			onMainProcessCreate();
		} else {
			onSecondaryProcessCreate(ProcessUtils.getProcessInfo(this));
		}
	}

	@MainThread
	protected void onMainProcessCreate() {
		// Do nothing
	}

	@MainThread
	protected void onSecondaryProcessCreate(ActivityManager.RunningAppProcessInfo processInfo) {
		// Do nothing
	}

	private boolean isDebuggable() {
		int flags = this.getApplicationInfo().flags;
		return (flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
	}

	protected Boolean isLoggingEnabled() {
		return isDebuggable() || (isFirebaseTestLabLoggingEnabled() && FirebaseTestLab.isRunningInstrumentedTests());
	}

	protected Boolean isFirebaseTestLabLoggingEnabled() {
		return true;
	}

	protected void initAppModule(Map<String, AppModule> appModulesMap) {
		// Do nothing
	}

	@MainThread
	@CallSuper
	@Override
	public final void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		if (!isMultiProcessSupportEnabled() || ProcessUtils.isMainProcess(this)) {
			ApplicationLifecycleHelper.onConfigurationChanged(this, newConfig);
			onMainProcessConfigurationChanged();
		} else {
			onSecondaryProcessConfigurationChanged(ProcessUtils.getProcessInfo(this));
		}
	}

	@MainThread
	protected void onMainProcessConfigurationChanged() {
		// Do nothing
	}

	@MainThread
	protected void onSecondaryProcessConfigurationChanged(ActivityManager.RunningAppProcessInfo processInfo) {
		// Do nothing
	}

	@MainThread
	@CallSuper
	@Override
	public final void onLowMemory() {
		super.onLowMemory();

		if (!isMultiProcessSupportEnabled() || ProcessUtils.isMainProcess(this)) {
			ApplicationLifecycleHelper.onLowMemory(this);
			onMainProcessLowMemory();
		} else {
			onSecondaryProcessLowMemory(ProcessUtils.getProcessInfo(this));
		}
	}

	@MainThread
	protected void onMainProcessLowMemory() {
		// Do nothing
	}

	@MainThread
	protected void onSecondaryProcessLowMemory(ActivityManager.RunningAppProcessInfo processInfo) {
		// Do nothing
	}

	@MainThread
	@CallSuper
	@Override
	public final void onTrimMemory(int level) {
		super.onTrimMemory(level);

		if (!isMultiProcessSupportEnabled() || ProcessUtils.isMainProcess(this)) {
			onMainProcessTrimMemory();
		} else {
			onSecondaryProcessTrimMemory(ProcessUtils.getProcessInfo(this));
		}
	}

	@MainThread
	protected void onMainProcessTrimMemory() {
		// Do nothing
	}

	@MainThread
	protected void onSecondaryProcessTrimMemory(ActivityManager.RunningAppProcessInfo processInfo) {
		// Do nothing
	}

	protected Boolean isMultiProcessSupportEnabled() {
		return BuildConfig.DEBUG && LeakCanaryHelper.isLeakCanaryEnabled();
	}

	@WorkerThread
	protected void verifyAppLaunchStatus() {
		Integer fromVersionCode = SharedPreferencesHelper.get().loadPreferenceAsInteger(VERSION_CODE_KEY);
		if (fromVersionCode == null) {
			appLaunchStatus = AppLaunchStatus.NEW_INSTALLATION;
		} else {
			if (AppUtils.getVersionCode().equals(fromVersionCode)) {
				appLaunchStatus = AppLaunchStatus.NORMAL;
			} else {
				appLaunchStatus = AppLaunchStatus.VERSION_UPGRADE;
			}
		}
		LOGGER.debug("App launch status: " + appLaunchStatus);
		if (!appLaunchStatus.equals(AppLaunchStatus.NORMAL)) {
			SharedPreferencesHelper.get().savePreferenceAsync(VERSION_CODE_KEY, AppUtils.getVersionCode());
		}

		if (appLaunchStatus.equals(AppLaunchStatus.VERSION_UPGRADE) && updateManager != null) {
			updateManager.update(fromVersionCode);
		}
	}

	private void initCoreAnalyticsSender() {
		coreAnalyticsTrackers.addAll(createCoreAnalyticsTrackers());
		coreAnalyticsSender = new CoreAnalyticsSender<>(coreAnalyticsTrackers);
	}

	protected List<? extends CoreAnalyticsTracker> createCoreAnalyticsTrackers() {
		return Lists.newArrayList();
	}

	@SuppressWarnings("unchecked")
	@NonNull
	public CoreAnalyticsSender<? extends CoreAnalyticsTracker> getCoreAnalyticsSender() {
		return coreAnalyticsSender;
	}

	protected void initStrictMode() {
		StrictModeHelper.initStrictMode();
	}

	public void initExceptionHandlers() {
		Class<? extends ExceptionHandler> exceptionHandlerClass = getExceptionHandlerClass();
		UncaughtExceptionHandler currentExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
		if (!currentExceptionHandler.getClass().equals(exceptionHandlerClass)) {
			ExceptionHandler exceptionHandler = ReflectionUtils.newInstance(exceptionHandlerClass);
			exceptionHandler.setDefaultExceptionHandler(defaultAndroidExceptionHandler);
			Thread.setDefaultUncaughtExceptionHandler(exceptionHandler);
			if (LoggerUtils.isEnabled()) {
				StringBuilder builder = new StringBuilder();
				builder.append(exceptionHandlerClass.getCanonicalName());
				builder.append(" initialized, wrapping ");
				builder.append(currentExceptionHandler.getClass().getCanonicalName());
				LOGGER.info(builder.toString());
				getCoreAnalyticsSender().trackErrorLog(builder.toString());
			}
		}
	}

	public ExceptionHandler getExceptionHandler() {
		if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ExceptionHandler)) {
			initExceptionHandlers();
		}
		return (ExceptionHandler)Thread.getDefaultUncaughtExceptionHandler();
	}

	public Class<? extends ExceptionHandler> getExceptionHandlerClass() {
		return DefaultExceptionHandler.class;
	}

	@WorkerThread
	public String getInstallationSource() {
		if (installationSource == null) {
			fetchInstallationSource();
		}
		return installationSource;
	}

	@WorkerThread
	private synchronized void fetchInstallationSource() {
		installationSource = SharedPreferencesHelper.get().loadPreference(INSTALLATION_SOURCE);
		if (StringUtils.isBlank(installationSource)) {
			installationSource = appContext.getInstallationSource();
			SharedPreferencesHelper.get().savePreference(INSTALLATION_SOURCE, installationSource);
		}
	}

	public abstract Class<? extends Activity> getHomeActivityClass();

	@NonNull
	protected AppContext createAppContext() {
		return new AppContext();
	}

	@NonNull
	public AppContext getAppContext() {
		return appContext;
	}

	@Nullable
	protected List<UpdateStep> createUpdateSteps() {
		return null;
	}

	@Nullable
	protected CacheManager createCacheManager() {
		return new CacheManager();
	}

	@Nullable
	public CacheManager getCacheManager() {
		synchronized (AbstractApplication.class) {
			if (cacheManager == null) {
				cacheManager = createCacheManager();
			}
		}
		return cacheManager;
	}

	@NonNull
	protected UriMapper createUriMapper() {
		return new UriMapper();
	}

	@NonNull
	public UriMapper getUriMapper() {
		return uriMapper;
	}

	@NonNull
	protected GitContext createGitContext() {
		return new AndroidGitContext();
	}

	@NonNull
	public GitContext getGitContext() {
		synchronized (AbstractApplication.class) {
			if (gitContext == null) {
				gitContext = createGitContext();
			}
		}
		return gitContext;
	}

	protected DebugContext createDebugContext() {
		return new DebugContext();
	}

	public DebugContext getDebugContext() {
		synchronized (AbstractApplication.class) {
			if (debugContext == null) {
				debugContext = createDebugContext();
			}
		}
		return debugContext;
	}

	public ActivityHelper createActivityHelper(AbstractFragmentActivity activity) {
		return new ActivityHelper(activity);
	}

	public FragmentHelper createFragmentHelper(Fragment fragment) {
		return new FragmentHelper(fragment);
	}

	/**
	 * @return the inBackground
	 */
	public Boolean isInBackground() {
		return activityLifecycleHandler == null || activityLifecycleHandler.isInBackground();
	}

	public Boolean isLoadingCancelable() {
		return false;
	}

	public String getAppName() {
		return getString(R.string.jdroid_appName);
	}

	public UserRepository getUserRepository() {
		return null;
	}

	private void initRepositories() {
		repositories = new HashMap<>();

		initRepositories(repositories);

		if (isDatabaseEnabled()) {
			SQLiteHelper dbHelper = new SQLiteHelper(this);
			getDebugContext().initDebugRepositories(repositories, dbHelper);
			initDatabaseRepositories(repositories, dbHelper);
			dbHelper.addUpgradeSteps(getSQLiteUpgradeSteps());
		}
	}

	protected void initRepositories(Map<Class<? extends Identifiable>, Repository<? extends Identifiable>> repositories) {
		// Do nothing
	}

	protected void initDatabaseRepositories(
		Map<Class<? extends Identifiable>, Repository<? extends Identifiable>> repositories, SQLiteHelper dbHelper) {
		// Do nothing
	}

	public Boolean isDatabaseEnabled() {
		return false;
	}

	protected List<SQLiteUpgradeStep> getSQLiteUpgradeSteps() {
		return Lists.newArrayList();
	}

	public Boolean isDebugLogRepositoryEnabled() {
		return false;
	}

	@SuppressWarnings("unchecked")
	public <M extends Identifiable> Repository<M> getRepositoryInstance(Class<M> persistentClass) {
		return (Repository<M>)repositories.get(persistentClass);
	}

	public AppLaunchStatus getAppLaunchStatus() {
		return appLaunchStatus;
	}

	public List<AppModule> getAppModules() {
		return Lists.newArrayList(appModulesMap.values());
	}

	public AppModule getAppModule(String appModuleName) {
		return appModulesMap.get(appModuleName);
	}

	public abstract int getLauncherIconResId();

	public abstract int getNotificationIconResId();

	public abstract String getManifestPackageName();

	public void addAppModulesMap(String name, AppModule appModule) {
		this.appModulesMap.put(name, appModule);
	}

	@MainThread
	@CallSuper
	public void onLocaleChanged() {
		NotificationUtils.createNotificationChannelsByType(getNotificationChannelTypes());
	}

	public void addCoreAnalyticsTracker(CoreAnalyticsTracker coreAnalyticsTracker) {
		this.coreAnalyticsTrackers.add(coreAnalyticsTracker);
	}

	public List<NotificationChannelType> getNotificationChannelTypes() {
		return Lists.newArrayList();
	}

	public RemoteConfigLoader getRemoteConfigLoader() {
		return remoteConfigLoader;
	}

	public void setRemoteConfigLoader(RemoteConfigLoader remoteConfigLoader) {
		this.remoteConfigLoader = remoteConfigLoader;
	}
}
