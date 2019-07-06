package com.jdroid.android.strictmode

import android.annotation.SuppressLint
import android.os.Build
import android.os.Handler
import android.os.StrictMode
import com.jdroid.android.context.BuildConfigUtils
import com.jdroid.android.firebase.testlab.FirebaseTestLab
import com.jdroid.android.utils.AppUtils
import com.jdroid.java.utils.LoggerUtils

/**
 * Initialize Android Strict Mode.
 * StrictMode is a developer tool which detects things you might be doing by accident and brings them to your attention so you can fix them.
 *
 *
 * https://developer.android.com/reference/android/os/StrictMode.html
 */
object StrictModeHelper {

    private val LOGGER = LoggerUtils.getLogger(StrictModeHelper::class.java)

    /*
	 * Initialize Strict Mode.
	 *
	 * Please invoke this method on Application#onCreate(), before calling super.onCreate()
	 */
    fun initStrictMode() {
        if (isStrictModeEnabled()) {
            LOGGER.debug("Initializing strict mode")
            innerInitStrictMode()
            //restore strict mode after onCreate() returns.
            Handler().postAtFrontOfQueue { innerInitStrictMode() }
        }
    }

    @SuppressLint("NewApi")
    private fun innerInitStrictMode() {
        val threadPolicyBuilder = StrictMode.ThreadPolicy.Builder()
        threadPolicyBuilder.detectAll()
        if (isStrictModeOnFirebaseTestLabEnabled() && isStrictModePenaltyDeath()) {
            // Android SDK and Google Maps is failing
            threadPolicyBuilder.permitDiskReads()
            threadPolicyBuilder.permitDiskWrites()
            threadPolicyBuilder.permitCustomSlowCalls()
        }
        threadPolicyBuilder.penaltyLog()
        if (isStrictModePenaltyDeath()) {
            threadPolicyBuilder.penaltyDeath()
        }
        StrictMode.setThreadPolicy(threadPolicyBuilder.build())

        val vmPolicyBuilder = StrictMode.VmPolicy.Builder()
        vmPolicyBuilder.detectActivityLeaks()
        vmPolicyBuilder.detectFileUriExposure()
        vmPolicyBuilder.detectLeakedRegistrationObjects()
        vmPolicyBuilder.detectLeakedSqlLiteObjects()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            vmPolicyBuilder.detectNonSdkApiUsage()
        }

        vmPolicyBuilder.penaltyLog()
        if (isStrictModePenaltyDeath()) {
            vmPolicyBuilder.penaltyDeath()
        }
        StrictMode.setVmPolicy(vmPolicyBuilder.build())
    }

    /*
	 * Strict mode shouldn't be enabled on production, but we enable it for the release APK if it is installed on Firebase Test Lab,
	 * so we can see the warnings detected by the tests.
	 *
	 * @return Whether strict mode is enabled or not.
	 */
    fun isStrictModeEnabled(): Boolean {
        return isStrictModeOnFirebaseTestLabEnabled() || !AppUtils.isReleaseBuildType() && BuildConfigUtils.getBuildConfigBoolean("STRICT_MODE_ENABLED", false)
    }

    fun isStrictModePenaltyDeath(): Boolean {
        return isStrictModeOnFirebaseTestLabEnabled() || BuildConfigUtils.getBuildConfigBoolean("STRICT_MODE_PENALTY_DEATH", false)
    }

    fun isStrictModeOnFirebaseTestLabEnabled(): Boolean {
        return FirebaseTestLab.isRunningInstrumentedTests() && BuildConfigUtils.getBuildConfigBoolean("STRICT_MODE_ON_FIREBASE_TEST_LAB_ENABLED", true)
    }
}
