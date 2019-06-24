package com.jdroid.android.firebase.testlab

import android.annotation.SuppressLint
import android.provider.Settings
import android.util.Log

import com.jdroid.android.BuildConfig
import com.jdroid.android.application.AbstractApplication

object FirebaseTestLab {

    private val TAG = FirebaseTestLab::class.java.simpleName

    private var isRunningInstrumentedTests: Boolean? = null

    @SuppressLint("LogCall")
    fun isRunningInstrumentedTests(): Boolean {
        if (isRunningInstrumentedTests == null) {
            try {
                val testLabSetting =
                    Settings.System.getString(AbstractApplication.get().contentResolver, "firebase.test.lab")
                isRunningInstrumentedTests = "true" == testLabSetting
            } catch (e: Exception) {
                isRunningInstrumentedTests = false
                if (BuildConfig.DEBUG) {
                    Log.w(TAG, "Error when getting firebase.test.lab system property", e)
                }
            } finally {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "Running Firebase Tests Labs: " + isRunningInstrumentedTests!!)
                }
            }
        }
        return isRunningInstrumentedTests!!
    }
}
