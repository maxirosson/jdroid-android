package com.jdroid.android.firebase.testlab

import android.annotation.SuppressLint
import android.provider.Settings
import android.util.Log

import com.jdroid.android.BuildConfig
import com.jdroid.android.application.AbstractApplication

object FirebaseTestLab {

    private val TAG = FirebaseTestLab::class.java.simpleName

    val isRunningInstrumentedTests: Boolean by lazy { fetchIsRunningInstrumentedTests() }

    @SuppressLint("LogCall")
    private fun fetchIsRunningInstrumentedTests(): Boolean {
        var runningInstrumentedTests = false
        try {
            val testLabSetting = Settings.System.getString(AbstractApplication.get().contentResolver, "firebase.test.lab")
            runningInstrumentedTests = "true" == testLabSetting
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "Error when getting firebase.test.lab system property", e)
            }
            runningInstrumentedTests = false
        } finally {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Running Firebase Tests Labs: $runningInstrumentedTests")
            }
        }
        return runningInstrumentedTests
    }
}
