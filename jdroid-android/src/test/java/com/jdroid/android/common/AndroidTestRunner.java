package com.jdroid.android.common;

import org.junit.runners.model.InitializationError;
import android.app.Activity;
import android.app.Application;
import com.jdroid.android.AbstractApplication;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricConfig;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.res.RobolectricPackageManager;
import com.xtremelabs.robolectric.shadows.ShadowApplication;

public class AndroidTestRunner extends RobolectricTestRunner {
	
	public AndroidTestRunner(Class<?> testClass) throws InitializationError {
		super(testClass);
	}
	
	/**
	 * @see com.xtremelabs.robolectric.RobolectricTestRunner#prepareTest(java.lang.Object)
	 */
	@Override
	public void prepareTest(Object test) {
		Robolectric.application.onCreate();
		super.prepareTest(test);
	}
	
	/**
	 * @see com.xtremelabs.robolectric.RobolectricTestRunner#setupApplicationState(com.xtremelabs.robolectric.RobolectricConfig)
	 */
	@Override
	public void setupApplicationState(RobolectricConfig robolectricConfig) {
		super.setupApplicationState(robolectricConfig);
		// Avoid null when asking the package name on tests
		ShadowApplication shadowApplication = Robolectric.shadowOf(Robolectric.application);
		shadowApplication.setPackageName(robolectricConfig.getPackageName());
		shadowApplication.setPackageManager(new RobolectricPackageManager(Robolectric.application, robolectricConfig));
	}
	
	@Override
	protected Application createApplication() {
		
		return new AbstractApplication() {
			
			@Override
			protected void initBitmapLruCache() {
			}
			
			@Override
			protected void initCacheDirectory() {
			}
			
			@Override
			protected void initImagesCacheDirectory() {
			}
			
			@Override
			public Class<? extends Activity> getHomeActivityClass() {
				return null;
			}
		};
	}
}