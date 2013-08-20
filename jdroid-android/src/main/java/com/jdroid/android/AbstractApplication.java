package com.jdroid.android;

import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.UUID;
import org.json.JSONObject;
import org.slf4j.Logger;
import roboguice.RoboGuice;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import com.crittercism.app.Crittercism;
import com.google.inject.AbstractModule;
import com.google.inject.util.Modules;
import com.jdroid.android.activity.BaseActivity;
import com.jdroid.android.analytics.AnalyticsSender;
import com.jdroid.android.analytics.AnalyticsTracker;
import com.jdroid.android.billing.BillingContext;
import com.jdroid.android.context.DefaultApplicationContext;
import com.jdroid.android.exception.DefaultExceptionHandler;
import com.jdroid.android.exception.ExceptionHandler;
import com.jdroid.android.fragment.BaseFragment;
import com.jdroid.android.gcm.GcmMessageResolver;
import com.jdroid.android.images.BitmapLruCache;
import com.jdroid.android.utils.AndroidEncryptionUtils;
import com.jdroid.android.utils.SharedPreferencesUtils;
import com.jdroid.android.utils.ToastUtils;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.parser.json.JsonObjectWrapper;
import com.jdroid.java.utils.DateUtils;
import com.jdroid.java.utils.ExecutorUtils;
import com.jdroid.java.utils.FileUtils;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.ReflectionUtils;

/**
 * 
 * @author Maxi Rosson
 */
public abstract class AbstractApplication extends Application {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(AbstractApplication.class);
	
	private static final String INSTALLATION_ID_KEY = "installationId";
	
	/** Maximum size (in MB) of the images cache */
	private static final int IMAGES_CACHE_SIZE = 5;
	
	private static final String IMAGES_DIRECTORY = "images";
	protected static AbstractApplication INSTANCE;
	
	private DefaultApplicationContext applicationContext;
	
	/** Current activity in the top stack. */
	private Activity currentActivity;
	
	private File cacheDirectory;
	private File imagesCacheDirectory;
	private BitmapLruCache bitmapLruCache;
	
	private String installationId;
	private boolean inBackground = false;
	
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
		
		LoggerUtils.setRelease(isDebuggable());
		
		loadInstallationId();
		
		applicationContext = createApplicationContext();
		
		if (applicationContext.displayDebugSettings()) {
			PreferenceManager.setDefaultValues(this, R.xml.debug_preferences, false);
		}
		initStrictMode();
		
		// This is required to initialize the statics fields of the utils classes.
		ToastUtils.init();
		DateUtils.init();
		
		initEncryptionUtils();
		initCacheDirectory();
		initImagesCacheDirectory();
		initBitmapLruCache();
		
		initInAppBilling();
		
		initRoboGuice();
		
		// TODO This is not working on the analytics beta3
		// initAnalytics();
	}
	
	/**
	 * @see android.app.Application#onTrimMemory(int)
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	public void onTrimMemory(int level) {
		super.onTrimMemory(level);
		
		if (level >= TRIM_MEMORY_MODERATE) {
			bitmapLruCache.evictAll();
		}
	}
	
	public Boolean hasAnalyticsSender() {
		return getAnalyticsSender() != null;
	}
	
	public <T extends AnalyticsTracker> AnalyticsSender<T> getAnalyticsSender() {
		return null;
	}
	
	public Boolean isStrictModeEnabled() {
		return true;
	}
	
	private void initStrictMode() {
		if (!applicationContext.isProductionEnvironment() && isStrictModeEnabled()) {
			StrictMode.enableDefaults();
		}
	}
	
	private void initCacheDirectory() {
		// Configure the cache dir for the whole application
		cacheDirectory = getExternalCacheDir();
		if (cacheDirectory == null) {
			// TODO We could listen the Intent.ACTION_DEVICE_STORAGE_LOW and clear the cache
			cacheDirectory = getCacheDir();
		}
		LOGGER.debug("Cache directory: " + cacheDirectory.getPath());
	}
	
	private void initImagesCacheDirectory() {
		imagesCacheDirectory = new File(getCacheDirectory(), IMAGES_DIRECTORY);
		LOGGER.debug("Images cache directory: " + imagesCacheDirectory.getPath());
		ExecutorUtils.execute(new Runnable() {
			
			@Override
			public void run() {
				if ((FileUtils.getDirectorySize(getImagesCacheDirectory()) / FileUtils.BYTES_TO_MB) > IMAGES_CACHE_SIZE) {
					FileUtils.forceDelete(imagesCacheDirectory);
				}
			}
		});
	}
	
	protected void initBitmapLruCache() {
		// Get memory class of this device, exceeding this amount will throw an OutOfMemory exception.
		int memClass = ((ActivityManager)getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		// Use 1/8th of the available memory for this memory cache.
		int cacheSize = (1024 * 1024 * memClass) / 8;
		bitmapLruCache = new BitmapLruCache(cacheSize);
	}
	
	private void initInAppBilling() {
		if (isInAppBillingEnabled()) {
			BillingContext.get().initialize();
		}
	}
	
	public void initExceptionHandlers() {
		initExceptionHandlers(null);
	}
	
	public void initExceptionHandlers(JsonObjectWrapper metadata) {
		UncaughtExceptionHandler currentExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
		if ((currentExceptionHandler == null) || !currentExceptionHandler.getClass().equals(getExceptionHandlerClass())) {
			initCrittercism(metadata);
			Thread.setDefaultUncaughtExceptionHandler(ReflectionUtils.newInstance(getExceptionHandlerClass()));
			LOGGER.debug("Custom exception handler initialized");
		}
	}
	
	private void initCrittercism(JsonObjectWrapper metadata) {
		
		if (applicationContext.isCrittercismEnabled()) {
			try {
				// send logcat data for devices with API Level 16 and higher
				JSONObject crittercismConfig = new JSONObject();
				crittercismConfig.put("shouldCollectLogcat", true);
				
				Crittercism.init(getApplicationContext(), applicationContext.getCrittercismAppId(), crittercismConfig);
				
				if (installationId != null) {
					Crittercism.setUsername(installationId);
				}
				if (metadata != null) {
					Crittercism.setMetadata(metadata.getJsonObject());
				}
				LOGGER.debug("Crittercism initialized");
			} catch (Exception e) {
				LOGGER.error("Error when initializing Crittercism");
			}
		}
	}
	
	protected void initEncryptionUtils() {
		// Init EncryptationUtils outside the UI thread
		ExecutorUtils.execute(new Runnable() {
			
			@Override
			public void run() {
				AndroidEncryptionUtils.init();
			}
		});
	}
	
	public ExceptionHandler getExceptionHandler() {
		return (ExceptionHandler)Thread.getDefaultUncaughtExceptionHandler();
	}
	
	public Class<? extends ExceptionHandler> getExceptionHandlerClass() {
		return DefaultExceptionHandler.class;
	}
	
	private void initRoboGuice() {
		AbstractModule androidModule = createAndroidModule();
		if (androidModule != null) {
			RoboGuice.setBaseApplicationInjector(this, RoboGuice.DEFAULT_STAGE,
				Modules.override(RoboGuice.newDefaultRoboModule(this)).with(androidModule));
		}
	}
	
	// private void initAnalytics() {
	// if (applicationContext.isAnalyticsEnabled()) {
	// GoogleAnalytics googleAnalytics = GoogleAnalytics.getInstance(getApplicationContext());
	// Tracker defaultTracker = googleAnalytics.getTracker(applicationContext.getAnalyticsTrackingId());
	// googleAnalytics.setDefaultTracker(defaultTracker);
	// // googleAnalytics.setDebug(true);
	// }
	// }
	
	/**
	 * @return the bitmapLruCache
	 */
	public BitmapLruCache getBitmapLruCache() {
		return bitmapLruCache;
	}
	
	public abstract Class<? extends Activity> getHomeActivityClass();
	
	protected AbstractModule createAndroidModule() {
		return null;
	}
	
	protected DefaultApplicationContext createApplicationContext() {
		return new DefaultApplicationContext();
	}
	
	public DefaultApplicationContext getAndroidApplicationContext() {
		return applicationContext;
	}
	
	public BaseActivity createBaseActivity(Activity activity) {
		return new BaseActivity(activity);
	}
	
	public BaseFragment createBaseFragment(Fragment fragment) {
		return new BaseFragment(fragment);
	}
	
	public Boolean isInAppBillingEnabled() {
		return false;
	}
	
	public void setCurrentActivity(Activity activity) {
		currentActivity = activity;
	}
	
	public Activity getCurrentActivity() {
		return currentActivity;
	}
	
	/**
	 * @return the cacheDirectory
	 */
	public File getCacheDirectory() {
		if (!cacheDirectory.exists()) {
			cacheDirectory.mkdirs();
		}
		return cacheDirectory;
	}
	
	public File getImagesCacheDirectory() {
		if (!imagesCacheDirectory.exists()) {
			imagesCacheDirectory.mkdirs();
		}
		return imagesCacheDirectory;
	}
	
	/**
	 * @return the inBackground
	 */
	public boolean isInBackground() {
		return inBackground;
	}
	
	/**
	 * @param inBackground the inBackground to set
	 */
	public void setInBackground(boolean inBackground) {
		this.inBackground = inBackground;
	}
	
	public static <T> T getInstance(Class<T> type) {
		return RoboGuice.getInjector(AbstractApplication.get()).getInstance(type);
	}
	
	public Boolean isLoadingCancelable() {
		return false;
	}
	
	public String getInstallationId() {
		return installationId;
	}
	
	private void loadInstallationId() {
		ExecutorUtils.execute(new Runnable() {
			
			@Override
			public void run() {
				try {
					if (SharedPreferencesUtils.hasPreference(INSTALLATION_ID_KEY)) {
						installationId = SharedPreferencesUtils.loadPreference(INSTALLATION_ID_KEY);
					} else {
						installationId = UUID.randomUUID().toString();
						SharedPreferencesUtils.savePreference(INSTALLATION_ID_KEY, installationId);
					}
					LOGGER.debug("Installation id: " + installationId);
				} catch (Exception e) {
					throw new UnexpectedException(e);
				}
			}
		});
	}
	
	public String getAppName() {
		return getString(R.string.appName);
	}
	
	private boolean isDebuggable() {
		int flags = this.getApplicationInfo().flags;
		return (flags & ApplicationInfo.FLAG_DEBUGGABLE) == 0;
	}
	
	public GcmMessageResolver getGcmResolver() {
		return null;
	}
}
