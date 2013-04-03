package com.jdroid.android.debug.mocks;

import com.jdroid.android.AbstractApplication;
import com.jdroid.android.debug.CrashGenerator;
import com.jdroid.java.http.mock.JsonMockWebService;

/**
 * 
 * @author Maxi Rosson
 */
public class AndroidJsonMockWebService extends JsonMockWebService {
	
	public AndroidJsonMockWebService(Object... urlSegments) {
		super(urlSegments);
	}
	
	@Override
	protected void simulateCrash() {
		String crashType = AbstractApplication.get().getAndroidApplicationContext().getHttpMockCrashType();
		if ((crashType != null) && !crashType.equals("None")) {
			CrashGenerator.crash(crashType, false);
		}
	}
	
	@Override
	protected Integer getHttpMockSleepDuration(Object... urlSegments) {
		return AbstractApplication.get().getAndroidApplicationContext().getHttpMockSleepDuration();
	}
	
}
