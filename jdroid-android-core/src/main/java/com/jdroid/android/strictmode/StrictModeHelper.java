package com.jdroid.android.strictmode;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;

import com.jdroid.android.context.BuildConfigUtils;
import com.jdroid.android.firebase.testlab.FirebaseTestLab;
import com.jdroid.android.utils.AppUtils;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

/**
 * Initialize Android Strict Mode.
 * StrictMode is a developer tool which detects things you might be doing by accident and brings them to your attention so you can fix them.
 *
 * https://developer.android.com/reference/android/os/StrictMode.html
 */
public class StrictModeHelper {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(StrictModeHelper.class);
	
	/*
	 * Initialize Strict Mode.
	 *
	 * Please invoke this method on Application#onCreate(), before calling super.onCreate()
	 */
	public static void initStrictMode() {
		if (isStrictModeEnabled()) {
			LOGGER.debug("Initializing strict mode");
			innerInitStrictMode();
			//restore strict mode after onCreate() returns.
			new Handler().postAtFrontOfQueue(new Runnable() {
				@Override
				public void run() {
					innerInitStrictMode();
				}
			});
		}
	}
	
	@SuppressLint("NewApi")
	private static void innerInitStrictMode() {
		StrictMode.ThreadPolicy.Builder threadPolicyBuilder = new StrictMode.ThreadPolicy.Builder();
		threadPolicyBuilder.detectAll();
		if (isStrictModeOnFirebaseTestLabEnabled() && isStrictModePenaltyDeath()) {
			// Android SDK and Google Maps is failing
			threadPolicyBuilder.permitDiskReads();
			threadPolicyBuilder.permitDiskWrites();
			threadPolicyBuilder.permitCustomSlowCalls();
		}
		threadPolicyBuilder.penaltyLog();
		if (isStrictModePenaltyDeath()) {
			threadPolicyBuilder.penaltyDeath();
		}
		StrictMode.setThreadPolicy(threadPolicyBuilder.build());
		
		StrictMode.VmPolicy.Builder vmPolicyBuilder = new StrictMode.VmPolicy.Builder();
		vmPolicyBuilder.detectActivityLeaks();
		vmPolicyBuilder.detectFileUriExposure();
		vmPolicyBuilder.detectLeakedRegistrationObjects();
		vmPolicyBuilder.detectLeakedSqlLiteObjects();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
			vmPolicyBuilder.detectNonSdkApiUsage();
		}
		
		vmPolicyBuilder.penaltyLog();
		if (isStrictModePenaltyDeath()) {
			vmPolicyBuilder.penaltyDeath();
		}
		StrictMode.setVmPolicy(vmPolicyBuilder.build());
	}
	
	
	/*
	 * Strict mode shouldn't be enabled on production, but we enable it for the release APK if it is installed on Firebase Test Lab,
	 * so we can see the warnings detected by the tests.
	 *
	 * @return Whether strict mode is enabled or not.
	 */
	public static Boolean isStrictModeEnabled() {
		return isStrictModeOnFirebaseTestLabEnabled() || !AppUtils.isReleaseBuildType() && BuildConfigUtils.getBuildConfigBoolean("STRICT_MODE_ENABLED", false);
	}
	
	public static Boolean isStrictModePenaltyDeath() {
		return isStrictModeOnFirebaseTestLabEnabled() || BuildConfigUtils.getBuildConfigBoolean("STRICT_MODE_PENALTY_DEATH", false);
	}
	
	public static Boolean isStrictModeOnFirebaseTestLabEnabled() {
		return FirebaseTestLab.isRunningInstrumentedTests() && BuildConfigUtils.getBuildConfigBoolean("STRICT_MODE_ON_FIREBASE_TEST_LAB_ENABLED", true);
	}
}
