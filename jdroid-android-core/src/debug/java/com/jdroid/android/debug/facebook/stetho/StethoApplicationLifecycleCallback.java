package com.jdroid.android.debug.facebook.stetho;

import android.content.Context;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.context.BuildConfigUtils;
import com.jdroid.android.http.HttpConfiguration;
import com.jdroid.android.lifecycle.ApplicationLifecycleCallback;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.http.HttpServiceFactory;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.ReflectionUtils;

import org.slf4j.Logger;

public class StethoApplicationLifecycleCallback extends ApplicationLifecycleCallback {

	private static final Logger LOGGER = LoggerUtils.getLogger(StethoApplicationLifecycleCallback.class);

	private static final String OKHTTP_SERVICE_FACTORY = "com.jdroid.java.http.okhttp.OkHttpServiceFactory";
	private static final String OKHTTP_INTERCEPTOR = "okhttp3.Interceptor";
	private static final String STETHO_OKHTTP_INTERCEPTOR = "com.facebook.stetho.okhttp3.StethoInterceptor";
	private static final String ADD_NETWORK_INTERCEPTOR = "addNetworkInterceptor";
	private static final String STETHO = "com.facebook.stetho.Stetho";
	private static final String INITIALIZE_WITH_DEFAULTS = "initializeWithDefaults";

	@Override
	public void onProviderInit(Context context) {
		try {
			HttpServiceFactory okhttpServiceFactory = ReflectionUtils.safeNewInstance(OKHTTP_SERVICE_FACTORY);
			if (okhttpServiceFactory != null) {
				Class<?> interceptorClass = ReflectionUtils.getClass(OKHTTP_INTERCEPTOR);
				Object stethoInterceptor = ReflectionUtils.newInstance(STETHO_OKHTTP_INTERCEPTOR);
				ReflectionUtils.invokeMethod(OKHTTP_SERVICE_FACTORY, okhttpServiceFactory, ADD_NETWORK_INTERCEPTOR,
					Lists.newArrayList(interceptorClass), Lists.newArrayList(stethoInterceptor));
				HttpConfiguration.INSTANCE.setHttpServiceFactory(okhttpServiceFactory);
				LOGGER.info("Stetho " + STETHO_OKHTTP_INTERCEPTOR + " initialized");
			}
		} catch (Exception e) {
			LOGGER.error("Error initializing " + STETHO_OKHTTP_INTERCEPTOR, e);
		}
	}

	@Override
	public void onCreate(Context context) {
		try {
			ReflectionUtils.invokeStaticMethod(STETHO, INITIALIZE_WITH_DEFAULTS,
				Lists.newArrayList(Context.class), Lists.newArrayList(AbstractApplication.get()));
			LOGGER.info("Stetho initialized");
		} catch (Exception e) {
			LOGGER.error("Error initializing Stetho", e);
		}
	}

	@Override
	public Boolean isEnabled() {
		return BuildConfigUtils.getBuildConfigBoolean("STETHO_ENABLED", false);
	}
}
