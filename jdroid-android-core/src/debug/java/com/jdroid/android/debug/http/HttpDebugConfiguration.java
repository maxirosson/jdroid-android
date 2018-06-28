package com.jdroid.android.debug.http;

import android.support.annotation.WorkerThread;

import com.jdroid.android.debug.mocks.AndroidJsonMockHttpService;
import com.jdroid.android.utils.SharedPreferencesHelper;
import com.jdroid.java.http.mock.AbstractMockHttpService;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

public class HttpDebugConfiguration {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(HttpDebugConfiguration.class);
	
	public static final String HTTP_MOCK_ENABLED = "httpMockEnabled";
	public static final String HTTP_MOCK_SLEEP = "httpMockSleep";
	
	private static boolean cachedHttpMockEnabled = false;
	
	public static boolean isHttpMockEnabled() {
		// Workaround for a random NullPointerException in unit tests using Robolectric 3.0 when calling setHttpMockEnabled(...)
		// in a method annotated with @Before. This NPE exception occurs randomly, at this point, when trying to access SharedPreferences.
		// For the workaround a variable is used to keep the value also in memory and a try-catch to handle the exception if it ocurrs.
		try {
			cachedHttpMockEnabled = SharedPreferencesHelper.get().loadPreferenceAsBoolean(HTTP_MOCK_ENABLED, false);
		} catch (Exception e) {
			LOGGER.error("Exception when reading sharedPreferences", e);
		}
		return cachedHttpMockEnabled;
	}
	
	@WorkerThread
	public static void setHttpMockEnabled(boolean enabled) {
		// Workaround for a random NullPointerException in unit tests using Robolectric 3.0 when calling setHttpMockEnabled(...)
		// in a method annotated with @Before. This NPE exception occurs randomly, at this point, when trying to access SharedPreferences.
		// For the workaround a variable is used to keep the value also in memory and a try-catch to handle the exception if it ocurrs.
		cachedHttpMockEnabled = enabled;
		try {
			SharedPreferencesHelper.get().savePreference(HTTP_MOCK_ENABLED, enabled);
		} catch(Exception e) {
			LOGGER.error("Exception when editing sharedPreferences", e);
		}
	}
	
	public static Integer getHttpMockSleepDuration() {
		return SharedPreferencesHelper.get().loadPreferenceAsBoolean(HTTP_MOCK_SLEEP, false) ? 10 : null;
	}
	
	public static AbstractMockHttpService getAbstractMockHttpServiceInstance(Object... urlSegments) {
		return new AndroidJsonMockHttpService(urlSegments);
	}
}
