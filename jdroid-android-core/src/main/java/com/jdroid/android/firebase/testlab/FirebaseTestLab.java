package com.jdroid.android.firebase.testlab;

import android.annotation.SuppressLint;
import android.provider.Settings;
import android.util.Log;

import com.jdroid.android.BuildConfig;
import com.jdroid.android.application.AbstractApplication;

public class FirebaseTestLab {

	private final static String TAG = FirebaseTestLab.class.getSimpleName();

	private static Boolean isRunningInstrumentedTests;

	@SuppressLint("LogCall")
	public static Boolean isRunningInstrumentedTests() {
		if (isRunningInstrumentedTests == null) {
			try {
				String testLabSetting = Settings.System.getString(AbstractApplication.get().getContentResolver(), "firebase.test.lab");
				isRunningInstrumentedTests = "true".equals(testLabSetting);
			} catch (Exception e) {
				isRunningInstrumentedTests = false;
				if (BuildConfig.DEBUG) {
					Log.w(TAG, "Error when getting firebase.test.lab system property", e);
				}
			} finally {
				if (BuildConfig.DEBUG) {
					Log.d(TAG, "Running Firebase Tests Labs: " + isRunningInstrumentedTests);
				}
			}
		}
		return isRunningInstrumentedTests;
	}

}
