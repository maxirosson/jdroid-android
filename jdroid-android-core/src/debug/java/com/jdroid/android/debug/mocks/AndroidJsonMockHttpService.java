package com.jdroid.android.debug.mocks;

import com.jdroid.android.debug.crash.CrashGenerator;
import com.jdroid.android.debug.crash.ExceptionType;
import com.jdroid.android.debug.http.HttpDebugConfiguration;
import com.jdroid.android.utils.SharedPreferencesHelper;
import com.jdroid.java.http.mock.AbstractMockHttpService;
import com.jdroid.java.http.mock.JsonMockHttpService;

public class AndroidJsonMockHttpService extends JsonMockHttpService {

	public static final String HTTP_MOCK_CRASH_TYPE = "httpMockCrashType";

	public AndroidJsonMockHttpService(Object... urlSegments) {
		super(urlSegments);
	}
	
	/**
	 * @see AbstractMockHttpService#simulateCrash()
	 */
	@Override
	protected void simulateCrash() {
		ExceptionType exceptionType = getHttpMockExceptionType();
		if (exceptionType != null) {
			CrashGenerator.crash(exceptionType, false);
		}
	}
	
	@Override
	protected Integer getHttpMockSleepDuration(Object... urlSegments) {
		return HttpDebugConfiguration.getHttpMockSleepDuration();
	}

	private ExceptionType getHttpMockExceptionType() {
		return ExceptionType.find(SharedPreferencesHelper.get().loadPreference(HTTP_MOCK_CRASH_TYPE));
	}
}
