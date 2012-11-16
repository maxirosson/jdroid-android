package com.jdroid.android;

import java.io.File;
import java.util.UUID;
import roboguice.RoboGuice;
import roboguice.application.RoboApplication;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import com.google.android.gcm.GCMRegistrar;
import com.google.inject.AbstractModule;
import com.google.inject.util.Modules;
import com.jdroid.android.activity.BaseActivity;
import com.jdroid.android.billing.BillingContext;
import com.jdroid.android.context.DefaultApplicationContext;
import com.jdroid.android.exception.DefaultExceptionHandler;
import com.jdroid.android.exception.ExceptionHandler;
import com.jdroid.android.fragment.BaseFragment;
import com.jdroid.android.images.BitmapLruCache;
import com.jdroid.android.utils.AlertDialogUtils;
import com.jdroid.android.utils.SharedPreferencesUtils;
import com.jdroid.android.utils.ToastUtils;
import com.jdroid.java.utils.DateUtils;
import com.jdroid.java.utils.ExecutorUtils;
import com.jdroid.java.utils.FileUtils;

/**
 * 
 * @author Maxi Rosson
 */
public abstract class AbstractApplication extends RoboApplication {
	
	private static final String INSTALLATION_ID_KEY = "installationId";
	
	/** Maximum size (in MB) of the images cache */
	private static final int IMAGES_CACHE_SIZE = 5;
	
	private static final String TAG = AbstractApplication.class.getSimpleName();
	
	private static final String IMAGES_DIRECTORY = "images";
	protected static AbstractApplication INSTANCE;
	
	private ExceptionHandler exceptionHandler;
	private DefaultApplicationContext applicationContext;
	
	/** Current activity in the top stack. */
	private Activity currentActivity;
	
	private File cacheDirectory;
	private File imagesCacheDirectory;
	private BitmapLruCache bitmapLruCache;
	
	private String installationId;
	private boolean inBackground = false;
	
	/** The {@link Runnable} to execute when the login activity is displayed */
	private Runnable loginRunnable;
	
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
		
		loadInstallationId();
		
		applicationContext = createApplicationContext();
		
		// This is required to initialize the statics fields of the utils classes.
		AlertDialogUtils.init();
		ToastUtils.init();
		DateUtils.init();
		
		initCacheDirectory();
		initImagesCacheDirectory();
		initBitmapLruCache();
		
		initInAppBilling();
		
		initExceptionHandler();
		
		initRoboGuice();
		
		initGCM();
		
		// TODO This is not working on the analytics beta3
		// initAnalytics();
	}
	
	/**
	 * @see android.app.Application#onTrimMemory(int)
	 */
	@Override
	public void onTrimMemory(int level) {
		super.onTrimMemory(level);
		
		if (level >= TRIM_MEMORY_MODERATE) {
			bitmapLruCache.evictAll();
		}
	}
	
	private void initCacheDirectory() {
		// Configure the cache dir for the whole application
		cacheDirectory = getExternalCacheDir();
		if (cacheDirectory == null) {
			// TODO We could listen the Intent.ACTION_DEVICE_STORAGE_LOW and clear the cache
			cacheDirectory = getCacheDir();
		}
	}
	
	private void initImagesCacheDirectory() {
		imagesCacheDirectory = new File(getCacheDirectory(), IMAGES_DIRECTORY);
		ExecutorUtils.execute(new Runnable() {
			
			@Override
			public void run() {
				if ((FileUtils.getDirectorySize(getImagesCacheDirectory()) / FileUtils.BYTES_TO_MB) > IMAGES_CACHE_SIZE) {
					FileUtils.forceDelete(imagesCacheDirectory);
				}
			}
		});
	}
	
	private void initBitmapLruCache() {
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
	
	private void initExceptionHandler() {
		exceptionHandler = createExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(exceptionHandler);
	}
	
	private void initRoboGuice() {
		RoboGuice.setBaseApplicationInjector(this, RoboGuice.DEFAULT_STAGE,
			Modules.override(RoboGuice.newDefaultRoboModule(this)).with(createAndroidModule()));
	}
	
	private void initGCM() {
		if (isGcmEnabled()) {
			try {
				GCMRegistrar.checkDevice(this);
			} catch (UnsupportedOperationException e) {
				Log.e(TAG, "This device does not support GCM", e);
			}
			// GCMRegistrar.checkManifest(this);
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
	
	protected ExceptionHandler createExceptionHandler() {
		return new DefaultExceptionHandler();
	}
	
	public ExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}
	
	protected AbstractModule createAndroidModule() {
		return new DefaultAndroidModule();
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
	
	public Boolean isGcmEnabled() {
		return false;
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
	
	/**
	 * @return the loginRunnable
	 */
	public Runnable getLoginRunnable() {
		return loginRunnable;
	}
	
	/**
	 * @param loginRunnable the loginRunnable to set
	 */
	public void setLoginRunnable(Runnable loginRunnable) {
		this.loginRunnable = loginRunnable;
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
					Log.d(TAG, "Installation id: " + installationId);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
	}
}
