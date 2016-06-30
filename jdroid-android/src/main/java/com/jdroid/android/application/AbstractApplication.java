package com.jdroid.android.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.jdroid.android.R;
import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.activity.ActivityHelper;
import com.jdroid.android.activity.ActivityLifecycleHandler;
import com.jdroid.android.analytics.AnalyticsSender;
import com.jdroid.android.analytics.AnalyticsTracker;
import com.jdroid.android.context.AndroidGitContext;
import com.jdroid.android.context.AppContext;
import com.jdroid.android.debug.DebugContext;
import com.jdroid.android.exception.DefaultExceptionHandler;
import com.jdroid.android.exception.ExceptionHandler;
import com.jdroid.android.fragment.FragmentHelper;
import com.jdroid.android.google.analytics.GoogleAnalyticsAppModule;
import com.jdroid.android.google.analytics.GoogleAnalyticsTracker;
import com.jdroid.android.http.cache.CacheManager;
import com.jdroid.android.images.loader.ImageLoaderHelper;
import com.jdroid.android.images.loader.uil.UilImageLoaderHelper;
import com.jdroid.android.repository.UserRepository;
import com.jdroid.android.sqlite.SQLiteHelper;
import com.jdroid.android.sqlite.SQLiteUpgradeStep;
import com.jdroid.android.uri.UriMapper;
import com.jdroid.android.utils.AppUtils;
import com.jdroid.android.utils.SharedPreferencesHelper;
import com.jdroid.android.utils.ToastUtils;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.collections.Maps;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.context.GitContext;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.domain.Identifiable;
import com.jdroid.java.repository.Repository;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.ReflectionUtils;
import com.jdroid.java.utils.StringUtils;

import org.slf4j.Logger;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.Kit;

public abstract class AbstractApplication extends Application {
	
	/**
	 * The LOGGER variable is initialized in the "OnCreate" method, after that "LoggerUtils" has been properly
	 * configured by the superclass.
	 */
	protected static Logger LOGGER;
	
	public static final String INSTALLATION_SOURCE = "installationSource";
	private static final String VERSION_CODE_KEY = "versionCodeKey";
	
	protected static AbstractApplication INSTANCE;
	
	private AppContext appContext;
	private GitContext gitContext;
	private DebugContext debugContext;

	private AnalyticsSender<? extends AnalyticsTracker> analyticsSender;
	private UriMapper uriMapper;
	
	/** Current activity in the top stack. */
	private Activity currentActivity;
	
	private AppLaunchStatus appLaunchStatus;
	
	private Map<Class<? extends Identifiable>, Repository<? extends Identifiable>> repositories;

	private Map<String, AppModule> appModulesMap = Maps.newLinkedHashMap();

	private ImageLoaderHelper imageLoaderHelper;

	private UpdateManager updateManager;
	private CacheManager cacheManager;

	private UncaughtExceptionHandler defaultAndroidExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

	private ActivityLifecycleHandler activityLifecycleHandler;

	public AbstractApplication() {
		INSTANCE = this;
	}
	
	public static AbstractApplication get() {
		return INSTANCE;
	}
	
	/**
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		
		appContext = createAppContext();

		LoggerUtils.setEnabled(appContext.isLoggingEnabled());
		LOGGER = LoggerUtils.getLogger(AbstractApplication.class);
		LOGGER.debug("Executing onCreate on " + this);

		// Strict mode
		if (appContext.isStrictModeEnabled()) {
			initStrictMode();
		}

		List<Kit> fabricKits = Lists.newArrayList();
		appModulesMap.put(GoogleAnalyticsAppModule.MODULE_NAME,  new GoogleAnalyticsAppModule());
		initAppModule(appModulesMap);
		for (AppModule each: appModulesMap.values()) {
			each.onCreate();
			fabricKits.addAll(each.getFabricKits());
		}
		fabricKits.addAll(getFabricKits());

		if (!fabricKits.isEmpty()) {
			Fabric.with(this, fabricKits.toArray(new Kit[0]));
		}

		analyticsSender = createAnalyticsSender(createAnalyticsTrackers());

		uriMapper = createUriMapper();

		updateManager = createUpdateManager();

		initExceptionHandlers();
		LoggerUtils.setExceptionLogger(getExceptionHandler());

		// This is required to initialize the statics fields of the utils classes.
		ToastUtils.init();
		DateUtils.init();

		imageLoaderHelper = createImageLoaderHelper();
		initRepositories();

		ExecutorUtils.execute(() -> {
            verifyAppLaunchStatus();

            if (getCacheManager() != null) {
                getCacheManager().initFileSystemCache();
            }

            if (imageLoaderHelper != null) {
                imageLoaderHelper.init();
            }
        });

		activityLifecycleHandler = new ActivityLifecycleHandler();
		registerActivityLifecycleCallbacks(activityLifecycleHandler);

	}

	@Nullable
	public ImageLoaderHelper getImageLoaderHelper() {
		return imageLoaderHelper;
	}

	@Nullable
	protected ImageLoaderHelper createImageLoaderHelper() {
		return new UilImageLoaderHelper();
	}

	protected void initAppModule(Map<String, AppModule> appModulesMap) {
		// Do nothing
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		for (AppModule each: appModulesMap.values()) {
			each.onConfigurationChanged(newConfig);
		}
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();

		for (AppModule each: appModulesMap.values()) {
			each.onLowMemory();
		}
	}

	@Override
	public void onTrimMemory(int level) {
		super.onTrimMemory(level);

		for (AppModule each: appModulesMap.values()) {
			each.onTrimMemory(level);
		}
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);

		for (AppModule each: appModulesMap.values()) {
			each.attachBaseContext(base);
		}
	}
	
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

	@NonNull
	protected AnalyticsSender<? extends AnalyticsTracker> createAnalyticsSender(List<? extends AnalyticsTracker> analyticsTrackers) {
		return new AnalyticsSender<>(analyticsTrackers);
	}

	public List<? extends AnalyticsTracker> createAnalyticsTrackers() {
		List<AnalyticsTracker> analyticsTrackers = Lists.newArrayList();
		AnalyticsTracker googleAnalyticsTracker = createGoogleAnalyticsTracker();
		if (googleAnalyticsTracker != null) {
			analyticsTrackers.add(googleAnalyticsTracker);
		}
		for (AppModule each: appModulesMap.values()) {
			analyticsTrackers.addAll(each.getAnalyticsTrackers());
		}
		return analyticsTrackers;
	}

	protected AnalyticsTracker createGoogleAnalyticsTracker() {
		return new GoogleAnalyticsTracker();
	}
	
	@SuppressWarnings("unchecked")
	@NonNull
	public AnalyticsSender<? extends AnalyticsTracker> getAnalyticsSender() {
		return analyticsSender;
	}
	
	protected void initStrictMode() {
		StrictMode.enableDefaults();
	}
	
	public void initExceptionHandlers() {
		getAnalyticsSender().onInitExceptionHandler(getExceptionHandlerMetadata());

		ExceptionHandler exceptionHandler = ReflectionUtils.newInstance(getExceptionHandlerClass());
		exceptionHandler.setDefaultExceptionHandler(defaultAndroidExceptionHandler);
		Thread.setDefaultUncaughtExceptionHandler(exceptionHandler);
		LOGGER.info("Custom exception handler initialized");
	}
	
	protected Map<String, String> getExceptionHandlerMetadata() {
		return null;
	}
	
	public ExceptionHandler getExceptionHandler() {
		return (ExceptionHandler)Thread.getDefaultUncaughtExceptionHandler();
	}
	
	public Class<? extends ExceptionHandler> getExceptionHandlerClass() {
		return DefaultExceptionHandler.class;
	}
	
	public void saveInstallationSource() {
		String installationSource = SharedPreferencesHelper.get().loadPreference(INSTALLATION_SOURCE);
		if (StringUtils.isBlank(installationSource)) {
			installationSource = appContext.getInstallationSource();
			SharedPreferencesHelper.get().savePreference(INSTALLATION_SOURCE, installationSource);
		}
	}
	
	public abstract Class<? extends Activity> getHomeActivityClass();

	@NonNull
	protected abstract AppContext createAppContext();

	@NonNull
	public AppContext getAppContext() {
		return appContext;
	}

	@Nullable
	protected UpdateManager createUpdateManager() {
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
	
	public void setCurrentActivity(Activity activity) {
		currentActivity = activity;
	}
	
	public Activity getCurrentActivity() {
		return currentActivity;
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
		return getString(R.string.appName);
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

	public List<Kit> getFabricKits() {
		return Lists.newArrayList();
	}

	public void initializeGcmTasks() {
		for (AppModule each: appModulesMap.values()) {
			each.onInitializeGcmTasks();
		}
	}

	public abstract int getLauncherIconResId();
}
