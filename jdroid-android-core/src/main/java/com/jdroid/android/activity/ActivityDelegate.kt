package com.jdroid.android.activity

import android.os.Bundle

abstract class ActivityDelegate(val activity: AbstractFragmentActivity) {

    open fun onCreate(savedInstanceState: Bundle?) {
        // Do Nothing
    }

    fun onStart() {
        // Do Nothing
    }

    open fun onResume() {
        // Do Nothing
    }

    open fun onBeforePause() {
        // Do Nothing
    }

    fun onPause() {
        // Do Nothing
    }

    fun onStop() {
        // Do Nothing
    }

    open fun onBeforeDestroy() {
        // Do Nothing
    }

    fun onDestroy() {
        // Do Nothing
    }
}
