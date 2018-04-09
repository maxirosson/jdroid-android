package com.jdroid.android.strictmode;

import android.os.Handler;
import android.os.StrictMode;

import com.jdroid.android.context.BuildConfigUtils;
import com.jdroid.android.firebase.testlab.FirebaseTestLab;
import com.jdroid.android.utils.AppUtils;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

public class StrictModeHelper {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(StrictModeHelper.class);
	
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
		vmPolicyBuilder.penaltyLog();
		if (isStrictModePenaltyDeath()) {
			vmPolicyBuilder.penaltyDeath();
		}
		StrictMode.setVmPolicy(vmPolicyBuilder.build());
	}
	
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
